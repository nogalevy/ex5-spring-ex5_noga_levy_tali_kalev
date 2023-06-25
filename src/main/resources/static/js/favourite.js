const cardsModule = (function () {
    let offset = 3; //TODO : const
    let totalNumOfFavourites = 0;

    const handleFlip = function (f, b) {
        f.classList.toggle('flipped')
        b.classList.toggle('flipped')
    }
    //NOGA: in general - add checkstatus function from last semester?
    const setNumOfFavourites = function (){
        fetch('/favourites/count')
            .then(response => {
                return response.json()
            })
            .then(data => {
                console.log("count", data);
                totalNumOfFavourites = data;
            })
            .catch(error => {
                console.error(error);
            });

    }
    const deleteJoke = function (jokeId) {
        console.log('Deleting joke with id: ' + jokeId);
        fetch('/favourites/delete', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: jokeId
        })
            .then(response => {
                console.log("here2", response);
                return response.json()
            })
            .then(deletedJokeId => {
                console.log("here", deletedJokeId);
                if (deletedJokeId !== null) {
                    offset -= 1;
                    totalNumOfFavourites -= 1;
                    const cardElement = document.getElementById(`card-${deletedJokeId}`);
                    if (cardElement) {
                        cardElement.remove();
                    }
                    checkRemoveLoadMoreBtn();
                    loadMoreFavourites(true);
                } else {
                    //TODO: handle error
                    console.error('Error deleting joke');
                }
            })
            .catch(error => {
                console.error(error);
            });
    }

    const checkRemoveLoadMoreBtn =  function (){
        const element = document.getElementById("loadMoreContainer");
        if(totalNumOfFavourites <= offset && element){
            element.remove();
        }
    }

    const loadMoreFavourites = function (afterDelete) {
        let query = '?offset=' + offset;
        if (afterDelete) query += '&limit=1';
        fetch('/favourites/get' + query)
            .then(response => response.json())
            .then(favourites => {
                offset += favourites.length;
                addCards(favourites);
                checkRemoveLoadMoreBtn();
            })
            .catch(error => {
                console.log(error);
            });
    }

    const addCards = function (data) {
        data.forEach((fav) => {
            let newDiv = document.createElement('div');
            newDiv.id = 'card-' + fav.id;
            newDiv.className = "col card-con";
            newDiv.setAttribute("type", fav.type);

            let html = getCardHtml(fav);
            let container = document.getElementById("favouritesContainer");
            container.appendChild(newDiv);
            newDiv.innerHTML = html;

            if (fav.type === 'twopart') addCardEvent(newDiv)
            addDeleteButtonEvent(newDiv);
        })

    }

    const addCardEvent = function (card) {
        const f = document.querySelector('#' + card.id + ' #front');
        const b = document.querySelector('#' + card.id + ' #back');
        card.addEventListener('click', () => handleFlip(f, b))
    }

    const getCardHtml = function (fav) {
        return `<div>
                    <div class='card-container'>
                        <div id='clickable' class='joke-card ${fav.type === 'twopart' ? 'flip-btn' : 'not-flip'}' >
                            <div id="back" class="cardBack overflow-x-hidden overflow-y-auto d-flex justify-content-center align-items-center"> 
                              <div class="text-center">
                                <h4 id="backCardContent">${fav.delivery}</h4>
                            </div>
                            </div>
                            <div id="front" class="cardFront overflow-x-hidden overflow-y-auto d-flex justify-content-center align-items-center">
                              <div class="text-center">
                                  <h4 id="frontCardContent">${fav.type === 'twopart' ? fav.setup : fav.joke}</h4>
                                  <p class="flip-msg">Click to flip</p>
                            </div>
                            </div>
                        </div>
                    </div>
                    <div class="btn delete-btn" data-joke-id="${fav.id}">
                         <i class="fa-solid fa-trash"></i>
                    </div>
                </div>`
    }

    const deleteButtonEvent = function (button) {
        const jokeId = button.getAttribute('data-joke-id');
        deleteJoke(jokeId);
    }

    const addDeleteButtonEvent = function (card) {
        const deleteButton = card.querySelector('.delete-btn');
        deleteButton.addEventListener('click', () => deleteButtonEvent(deleteButton));
    }

    return {
        loadMoreFavourites,
        addCardEvent,
        addDeleteButtonEvent,
        setNumOfFavourites
    }
})();


// --------------------------------------------------------------
/**
 * upon loading the page, we bind handlers to the form and the button
 */
(function () {
    document.addEventListener("DOMContentLoaded", () => {
        (function (){
            cardsModule.setNumOfFavourites();
        })();
        let loadMoreBtn = document.getElementById("loadMore");
        document.querySelectorAll('.card-con').forEach(card => {
            if (card.getAttribute("type") === 'twopart')
                cardsModule.addCardEvent(card);
            cardsModule.addDeleteButtonEvent(card);
        });

        if(loadMoreBtn) loadMoreBtn.addEventListener('click',() => cardsModule.loadMoreFavourites(false));
    })
}());