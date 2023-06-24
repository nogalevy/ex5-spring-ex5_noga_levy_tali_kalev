const cardsModule = (function () {
    let offset = 3; //TODO : const

    const handleFlip = function (f, b) {
        f.classList.toggle('flipped')
        b.classList.toggle('flipped')
    }

    function deleteJoke(jokeId) {
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
                    //NOGA: also need to offset -= 1 i think
                    offset -= 1;
                    const cardElement = document.getElementById(`card-${deletedJokeId}`);
                    if (cardElement) {
                        cardElement.remove();
                    }
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

    const loadMoreFavourites = function (afterDelete) {
        let query = '?offset=' + offset;
        if (afterDelete) query += '&limit=1';
        fetch('/favourites/get' + query)
            .then(response => response.json())
            .then(favourites => {
                offset += favourites.length;
                addCards(favourites);
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
                            <div id="back" class="cardBack"> 
                                <h3 id="backCardContent">${fav.delivery}</h3>
                            </div>
                            <div id="front" class="cardFront">
                              <h3 id="frontCardContent">${fav.type === 'twopart' ? fav.setup : fav.joke}</h3>
                              <p class="flip-msg">Click to flip</p>
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
        addDeleteButtonEvent
    }
})();


// --------------------------------------------------------------
/**
 * upon loading the page, we bind handlers to the form and the button
 */
(function () {
    document.addEventListener("DOMContentLoaded", () => {

        document.querySelectorAll('.card-con').forEach(card => {
            if (card.getAttribute("type") === 'twopart')
                cardsModule.addCardEvent(card);
            cardsModule.addDeleteButtonEvent(card);
        });
        document.getElementById("loadMore").addEventListener('click',() => cardsModule.loadMoreFavourites(false));
    })
}());