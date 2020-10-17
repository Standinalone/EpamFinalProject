document.addEventListener('DOMContentLoaded', listen);

function listen() {
	var endDate = document.getElementById("enddate");
	var startDate = document.getElementById("startdate");
	if (endDate != null && startDate != null) {
		endDate.onchange = function() {
			validateDates(startDate, endDate)
		}
		startDate.onchange = function() {
			validateDates(startDate, endDate)
		}
	}
}

function validateDates(startDate, endDate){
	var duration = document.getElementById("duration");
	var start = new Date(startDate.value)
	var end = new Date(endDate.value)
	var dayDiff = (end - start) / (1000 * 60 * 60 * 24)
	if (dayDiff < 1){
		duration.classList.add("text-danger")
	}
	else{
		duration.classList.remove("text-danger")
	}
	duration.innerHTML = dayDiff
}