/**
 *  gets utils id and show it
 * @param toastId utils id to show
 */
export function toast (toastId){
    if(!toastId) return;
    const toastLiveExample = document.getElementById(toastId);
    if(toastLiveExample){
        toastLiveExample.classList.add("show");
        toastLiveExample.classList.remove("hide");
        setTimeout(()=>{
            toastLiveExample.classList.add("hide");
            toastLiveExample.classList.remove("show");
        }, 4000)
    }
}

/**
 * check the status code of fetch request
 * @param response a response from fetching that data we want to check its status
 * @returns {Promise} error if statuscode > 200 else the response
 */
export function checkStatus (response) {
    if (response.status >= 200 && response.status < 300) {
        return Promise.resolve(response);
    } else {
        return Promise.reject(new Error(response.statusText));
    }
}

