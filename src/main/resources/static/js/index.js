import { toast, checkStatus } from './utils.js';
import { TWO_PART_TYPE } from "./consts.js";

/**
 * This module is responsible for the main page.
 * @type {{generateNew: generateNew, handleFlip: handleFlip, handleInitialButtonState: handleInitialButtonState}}
 */
const mainModule = (function () {
    let isGenerate = false;
    let generateNewLoader;

    /**
     * This function is responsible for setting the initial state of the like button.
     * @param isFavourite - boolean. true if the joke is favourite, false otherwise
     */
    const handleInitialButtonState = function (isFavourite) {
        generateNewLoader = document.getElementById("gengenerateNewLoader")
        const buttonIcon = document.getElementById("buttonIcon");
        let addBtn = document.getElementById("addFavourite");
        addBtn.setAttribute("data-isFavourite", isFavourite);
        if (isFavourite) {
            buttonIcon.classList.remove("fa-regular");
            buttonIcon.classList.add("fa-solid");
            addBtn.removeEventListener('click', add);
        } else {
            buttonIcon.classList.remove("fa-solid");
            buttonIcon.classList.add("fa-regular");
            addBtn.addEventListener('click', add);
        }
    }

    /**
     * This function is responsible for flipping the card.
     * @param e - event
     */
    const handleFlip = function (e) {
        const card = document.getElementById('clickable');
        if (!card.classList.contains('flip-btn')) return;
        flipCard(true);
    }

    /**
     * This function is responsible for displaying the loader.
     * @param display - boolean. true if we want to display the loader, false otherwise
     */
    const displayGenerateNewLoader = function (display = false) {
        if (display) generateNewLoader.classList.remove("visually-hidden");
        else generateNewLoader.classList.add("visually-hidden");
    }

    /**
     * This function generates a new joke by requesting it from the server.
     */
    const generateNew = function () {
        if (isGenerate) return;
        isGenerate = true;
        const card = document.getElementById('clickable');
        let cardBox = document.getElementById("cardBox");
        let frontContent = document.getElementById('frontCardContent');
        let backContent = document.getElementById('backCardContent');
        flipCard();
        displayGenerateNewLoader(true);

        fetch('/api/getJokes')
            .then(checkStatus)
            .then(response => response.json())
            .then(jsonData => {
                cardBox.setAttribute("name", jsonData.id);
                if (jsonData.type === TWO_PART_TYPE) {
                    frontContent.innerHTML = jsonData.setup;
                    backContent.innerHTML = jsonData.delivery;
                    card.classList.add('flip-btn');
                }
                else {
                    frontContent.innerHTML = jsonData.joke;
                    backContent.innerHTML = "";
                    card.classList.remove('flip-btn')
                    card.classList.add('not-flip')
                }
                handleInitialButtonState(jsonData.isFavourite);
            })
            .catch(error => {
                toast("errorToast");
            })
            .finally(() => {
                displayGenerateNewLoader(false);
                isGenerate = false
            })
    }

    /**
     * This function is responsible for flipping the card.
     * @param toggle boolean . true if we want to toggle the card. false if just want to force cover it
     */
    const flipCard = function (toggle = false) {
        const front = document.getElementById('front')
        const back = document.getElementById('back')
        if (toggle || front.classList.contains('flipped')) {
            front.classList.toggle('flipped')
            back.classList.toggle('flipped')
        }
    }

    /**
     * This function is responsible for adding a joke to the favourites.
     */
    const add = function () {
        let cardBox = document.getElementById("cardBox");
        let jokeId = cardBox.getAttribute("name");
        fetch('/api/favourites/add', {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: jokeId
        })
            .then(checkStatus)
            .then(response => {
                handleInitialButtonState(true);
            })
            .catch(error => {
                toast("errorToast");
            });
    }

    return {
        generateNew,
        handleInitialButtonState,
        handleFlip
    }
})()

// --------------------------------------------------------------
/**
 * upon loading the page, we bind handlers
 */
document.addEventListener("DOMContentLoaded", () => {

    let addBtn = document.getElementById("addFavourite");
    const buttonIcon = document.getElementById("buttonIcon");

    // init event handler on buttons
    (function () {
        const isFavourite = addBtn.getAttribute("data-isFavourite");
        mainModule.handleInitialButtonState(isFavourite === "true");
    })();

    // click events
    document.getElementById('clickable').addEventListener('click', mainModule.handleFlip);
    document.getElementById("generate").addEventListener("click", mainModule.generateNew);

    // hover event on button
    addBtn.addEventListener('mouseenter', () => {
        buttonIcon.classList.add("fa-beat");
    });

    // hover event on button
    addBtn.addEventListener('mouseleave', () => {
        buttonIcon.classList.remove("fa-beat");
    });

});