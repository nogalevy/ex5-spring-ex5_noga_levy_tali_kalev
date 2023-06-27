import {toast, checkStatus} from './utils.js';

const mainModule = (function (){
    const handleInitialButtonState = function (isFavourite) {
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

    const handleFlip = function (e) {
        const card = document.getElementById('clickable');
        if(!card.classList.contains('flip-btn')) return;
        flipCard(true);
    }

    const generateNew = function(){
        const card = document.getElementById('clickable');
        let cardBox = document.getElementById("cardBox");
        let frontContent = document.getElementById('frontCardContent');
        let backContent = document.getElementById('backCardContent');

        fetch('/api/getJokes')
        .then(checkStatus)
        .then(response => response.json())
        .then(jsonData => {
            cardBox.setAttribute("name", jsonData.id);
            flipCard();
            //TODO: add to constantnsnsts!
            if (jsonData.type === "twopart") {
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
            console.log(error);
        });
    }

    /**
     *
     * @param toggle boolean . true if we want to toggle the card. false if just want to force cover it
     */
    const flipCard = function (toggle = false){
        const front = document.getElementById('front')
        const back = document.getElementById('back')
        if(toggle || front.classList.contains('flipped')){
            front.classList.toggle('flipped')
            back.classList.toggle('flipped')
        }
    }

    const add = function (){
        let cardBox = document.getElementById("cardBox");
        let jokeId = cardBox.getAttribute("name");
        fetch('/favourites/add', {
                method: "POST",
                headers: {"Content-Type": "application/json"},
                body: jokeId
            })
            .then(checkStatus)
            .then(response =>
            {
                handleInitialButtonState(true);
            })
            .catch(error => {
                console.log("could not add ", error);
                toast("errorToast");
            });
    }

    return {
        generateNew,
        handleInitialButtonState,
        handleFlip
    }
})()

document.addEventListener("DOMContentLoaded", () => {
    let addBtn = document.getElementById("addFavourite");
    const buttonIcon = document.getElementById("buttonIcon");

    (function (){
        const isFavourite = addBtn.getAttribute("data-isFavourite"); //NOGA: we can check if contain specific classname
        mainModule.handleInitialButtonState(isFavourite === "true"); //NOGA
    })();

    document.getElementById('clickable').addEventListener('click', mainModule.handleFlip);
    document.getElementById("generate").addEventListener("click", mainModule.generateNew);

    addBtn.addEventListener('mouseenter', () => {
        buttonIcon.classList.add("fa-beat");
    });

    addBtn.addEventListener('mouseleave', () => {
        buttonIcon.classList.remove("fa-beat");
    });

});