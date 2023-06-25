/**
 *  gets toast id and show it
 * @param toastId toast id to show
 */
export default function toast(toastId){
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

