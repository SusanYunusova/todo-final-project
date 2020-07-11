const deleteTaskButtons = document.querySelectorAll(".task-icons-item");
deleteTaskButtons.forEach(button => button.addEventListener("click", showDeleteModal));

const overlay = document.querySelector(".overlay");
const deleteModal = document.querySelector(".modal");
deleteModal.addEventListener("click", function({target}){
    if(target.classList.contains("modal-close")) {
        this.classList.remove("open");
        overlay.classList.remove("show");
    }

});

function showDeleteModal(){

    overlay.classList.add("show");

    deleteModal.classList.add("open");
}


function loadDashBoard() {
    console.log("happy")
    var element = document.getElementById("task-type");
    var index = element.options[element.selectedIndex].value;


    window.location.href = '/api/sort-tasks/'+index;
}

function setSelection(index) {
    var element = document.getElementById("task-type");
    element.options[index].selected = true;
}