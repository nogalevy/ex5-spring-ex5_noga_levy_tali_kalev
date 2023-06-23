
const cardsModule = (function () {
    let offset = 3; //TODO : const

    const handleFlip = function(f,b) {
        f.classList.toggle('flipped')
        b.classList.toggle('flipped')
    }

    const loadMoreFavourites = function (){
        fetch('/favourites?offset=' + offset)
            .then(response => response.json() )
            .then(favourites => {
                offset += favourites.length;
                addCards(favourites);
            })
            .catch(error => {
                console.log(error);
            });
    }

    const addCards = function (data){
        data.forEach((fav)=>{
            let newDiv = document.createElement('div');
            newDiv.id = 'card-' + fav.id;
            newDiv.className =  "col card-con";
            newDiv.setAttribute("type", fav.type);

            let html = getCardHtml(fav);
            let container = document.getElementById("favouritesContainer");
            container.appendChild(newDiv);
            newDiv.innerHTML = html;

            if(fav.type === 'twopart') addCardEvent(newDiv)
        })

    }

    const addCardEvent= function (card){
        const f = document.querySelector('#' + card.id + ' #front');
        const b = document.querySelector('#' + card.id + ' #back');
        card.addEventListener('click', () => handleFlip(f,b))
    }

    const getCardHtml = function (fav){
        return `<div>
                    <div class='card-container'>
                    <div id='clickable' class='joke-card ${fav.type === 'twopart' ? 'flip-btn' : 'not-flip'}' >
                        <div id="back" class="cardBack"> 
                         <h3 id="backCardContent">${fav.delivery}</h3>
                        </div>
                      <div id="front" class="cardFront">
                          <h3 id="frontCardContent">${fav.type === 'twopart' ? fav.setup : fav.joke }</h3>
                          <p class="flip-msg">Click to flip</p>
                         </div>
                    </div>
              </div>
            </div>
             <div>delete</div>`
    }

    return{
        loadMoreFavourites,
        addCardEvent
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
        });
        document.getElementById("loadMore").addEventListener('click', cardsModule.loadMoreFavourites);
    })
}());