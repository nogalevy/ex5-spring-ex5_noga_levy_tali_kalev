import { toast, checkStatus } from './utils.js';
import {DEFAULT_OFFSET, TWO_PART_TYPE} from "./consts.js";

/**
 * This module is responsible for the favourite jokes page.
 * @type {{addDeleteButtonEvent: addDeleteButtonEvent, addCardEvent: addCardEvent, initModule: initModule, loadMoreFavourites: loadMoreFavourites}}
 */
const cardsModule = (function () {
    let offset = DEFAULT_OFFSET;
    let totalNumOfFavourites = 0;
    let loader;

    /**
     * This function is responsible for flipping the card.
     * @param f
     * @param b
     */
    const handleFlip = function (f, b) {
        f.classList.toggle('flipped')
        b.classList.toggle('flipped')
    }

    /**
     * This function sets num of favourites for load more button
     */
    const initModule = function () {
        loader = document.getElementById("pageLoader");
        setNumOfFavourites()
    }

    /**
     * This function retrieves number of favourites from the server.
     */
    const setNumOfFavourites = function () {
        fetch('/api/favourites/count')
            .then(checkStatus)
            .then(response => {
                return response.json()
            })
            .then(data => {
                totalNumOfFavourites = data;
            })
            .catch(error => {
                toast("loadMoreErrorToast");
            });
    }

    /**
     * Function displays message for user to like jokes if list is empty
     */
    const checkEmptyList = function () {
        if (totalNumOfFavourites !== 0) return;
        const msg = document.getElementById("emptyList");
        msg.classList.remove("d-none")
    }

    /**
     * Function deletes joke from server + DOM and completes checks for load more button and empty list
     * @param jokeId - id of joke to delete
     */
    const deleteJoke = function (jokeId) {
        let currLoader = document.getElementById("deleteLoader-" + jokeId)
        let currDeleteBtn = document.getElementById("deleteBtn-" + jokeId)
        showElement(currLoader, true)
        showElement(currDeleteBtn, false)
        fetch('/api/favourites/delete', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: jokeId
        })
            .then(checkStatus)
            .then(response => response.json())
            .then(deletedJokeId => {
                if (deletedJokeId !== null) {
                    offset -= 1;
                    totalNumOfFavourites -= 1;
                    const cardElement = document.getElementById(`card-${deletedJokeId}`);
                    if (cardElement) cardElement.remove();
                    checkRemoveLoadMoreBtn();
                    checkEmptyList();
                    loadMoreFavourites(true);
                }
                else {
                    toast("deleteErrorToast");
                }
            })
            .catch(error => {
                toast("deleteErrorToast");
            }).finally(() => {
                showElement(currLoader, false)
                showElement(currDeleteBtn, true)

            })
    }

    /**
     * Function checks if load more button should be removed
     */
    const checkRemoveLoadMoreBtn = function () {
        const element = document.getElementById("loadMoreContainer");
        if (totalNumOfFavourites <= offset && element) {
            element.remove();
        }
    }

    /**
     * Checks if to show element
     * @param element - element to show
     * @param show - boolean if to show
     */
    const showElement = function (element, show) {
        if (show) element.classList.remove("visually-hidden");
        else element.classList.add("visually-hidden");
    }

    /**
     * Function sends request to server to receive more favourites
     * @param afterDelete - boolean if request is after deleting a favourite
     */
    const loadMoreFavourites = function (afterDelete) {
        let query = '?offset=' + offset;
        if (afterDelete) query += '&limit=1';
        showElement(loader, true);
        fetch('/api/favourites/get' + query)
            .then(checkStatus)
            .then(response => response.json())
            .then(favourites => {
                offset += favourites.length;
                addCards(favourites);
                checkRemoveLoadMoreBtn();
            })
            .catch(error => {
                toast("loadMoreErrorToast");
            })
            .finally(() => {
                showElement(loader, false);
            })
    }

    /**
     * Function adds more favourites to DOM
     * @param data
     */
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

            if (fav.type === TWO_PART_TYPE) addCardEvent(newDiv)
            addDeleteButtonEvent(newDiv);
        })
    }

    /**
     * Function adds event listener to cards for flipping
     * @param card
     */
    const addCardEvent = function (card) {
        const f = document.querySelector('#' + card.id + ' #front');
        const b = document.querySelector('#' + card.id + ' #back');
        card.addEventListener('click', () => handleFlip(f, b))
    }

    /**
     * Function builds html for card
     * @param fav - favourite to create card for
     * @returns {string} - html for card
     */
    const getCardHtml = function (fav) {
        return `<div>
                    <div class='card-container p-3 pb-2'>
                        <div id='clickable' class='text-break joke-card ${fav.type === TWO_PART_TYPE ? 'flip-btn' : 'not-flip'}' >
                            <div id="back" class="cardBack overflow-x-hidden overflow-y-auto d-flex justify-content-center align-items-center"> 
                              <div class="text-center mh-100">
                                <h4 id="backCardContent">${fav.delivery}</h4>
                               </div>
                            </div>
                            <div id="front" class="cardFront overflow-x-hidden overflow-y-auto d-flex justify-content-center align-items-center">
                              <div class="text-center mh-100">
                                  <h4 id="frontCardContent">${fav.type === TWO_PART_TYPE ? fav.setup : fav.joke}</h4>
                                  <p class="flip-msg">Click to flip</p>
                            </div>
                            </div>
                        </div>
                    </div>
                   <div class="d-flex justify-content-center m-3 mt-0 rounded-bottom card-footer">
                        <div id='deleteBtn-${fav.id}' class="w-100 btn delete-btn" data-joke-id=${fav.id}>
                             <i class="fa-solid fa-trash"></i>
                       </div>
                        <div id='deleteLoader-${fav.id}' class="visually-hidden">
                            <div class="spinner-border text-dark" role="status"></div>
                        </div>
                    </div>
                </div>`
    }

    /**
     * Function handles event of clicking on delete button
     * @param button
     */
    const deleteButtonEvent = function (button) {
        const jokeId = button.getAttribute('data-joke-id');
        deleteJoke(jokeId);
    }

    /**
     * Function adds event listener to delete button
     * @param card - card button to add event listener to
     */
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
        (function () {
            cardsModule.initModule();
        })();
        let loadMoreBtn = document.getElementById("loadMore");
        document.querySelectorAll('.card-con').forEach(card => {
            if (card.getAttribute("type") === 'twopart')
                cardsModule.addCardEvent(card);
            cardsModule.addDeleteButtonEvent(card);
        });

        if (loadMoreBtn) loadMoreBtn.addEventListener('click', () => cardsModule.loadMoreFavourites(false));
    })
}());