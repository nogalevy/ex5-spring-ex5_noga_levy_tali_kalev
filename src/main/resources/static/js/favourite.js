import toast from './toast.js';

const cardsModule = (function () {
    let offset = 3; //TODO : const
    let totalNumOfFavourites = 0;
    let loader;

    const handleFlip = function (f, b) {
        f.classList.toggle('flipped')
        b.classList.toggle('flipped')
    }

    const initModule = function (){
        loader = document.getElementById("pageLoader");
        setNumOfFavourites()
    }

    // /**
    //  *  gets toast id and show it
    //  * @param toastId toast id to show
    //  */
    // const toast = function (toastId){
    //     if(!toastId) return;
    //     const toastLiveExample = document.getElementById(toastId);
    //     if(toastLiveExample){
    //         toastLiveExample.classList.add("show");
    //         toastLiveExample.classList.remove("hide");
    //         setTimeout(()=>{
    //             toastLiveExample.classList.add("hide");
    //             toastLiveExample.classList.remove("show");
    //         }, 2000)
    //     }
    // }

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
        let currLoader = document.getElementById("deleteLoader-" + jokeId)
        showElement(currLoader, true)
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
                toast("deleteErrorToast");
            }).finally(()=>{
                showElement(currLoader, false)
        })
    }

    const checkRemoveLoadMoreBtn =  function (){
        const element = document.getElementById("loadMoreContainer");
        if(totalNumOfFavourites <= offset && element){
            element.remove();
        }
    }

    const showElement = function (element, show){
        if(show) element.classList.remove("visually-hidden");
        else element.classList.add("visually-hidden");
    }

    const loadMoreFavourites = function (afterDelete) {
        let query = '?offset=' + offset;
        if (afterDelete) query += '&limit=1';
        showElement(loader, true);
        fetch('/favourites/get' + query)
            .then(response => response.json())
            .then(favourites => {
                offset += favourites.length;
                addCards(favourites);
                checkRemoveLoadMoreBtn();
            })
            .catch(error => {
                toast("loadMoreErrorToast");
                console.log(error);
            })
            .finally(()=>{
                showElement(loader, false);
            })
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
                   <div class="d-flex">
                        <div id="deleteBtn" class="btn delete-btn" data-joke-id=${fav.id}>
                             <i class="fa-solid fa-trash"></i>
                       </div>
                        <div id='deleteLoader-${fav.id}' class="visually-hidden">
                            <div class="spinner-border text-dark" role="status"></div>
                        </div>
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
        initModule
    }
})();


// --------------------------------------------------------------
/**
 * upon loading the page, we bind handlers to the form and the button
 */
(function () {
    document.addEventListener("DOMContentLoaded", () => {
        (function (){
            cardsModule.initModule();
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