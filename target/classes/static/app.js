
var autoRefreshIntervalId = null;

const containerPanel = document.getElementById("visualization");
const containerTimelineOptions = {
    timeAxis: {scale: "day"},
    orientation: {axis: "top"},
    stack: false,
    xss: {disabled: true}, // Items are XSS safe through JQuery
    zoomMin: 3 * 1000 * 60 * 60 * 24 // Three day in milliseconds
};
var containerGroupDataSet = new vis.DataSet();
var containerItemDataSet = new vis.DataSet();
var containerTimeline = new vis.Timeline(containerPanel, containerItemDataSet, containerGroupDataSet, containerTimelineOptions);

var prototypes = [];
$(document).ready(function () {
    $.ajaxSetup({
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        }
    });
    // Extend jQuery to support $.put() and $.delete()
    jQuery.each(["put", "delete"], function (i, method) {
        jQuery[method] = function (url, data, callback, type) {
            if (jQuery.isFunction(data)) {
                type = type || callback;
                callback = data;
                data = undefined;
            }
            return jQuery.ajax({
                url: url,
                type: method,
                dataType: type,
                data: data,
                success: callback
            });
        };
    });
    
    
    $("#resolvetButton").click(function() {
		solve();
	});

	$("#resourceButton").click(function() {
		uplaodResource();
	});
	
    $("#solveButton").click(function () {
        solve();
    });
	
	$("#refreshButton").click(function() {
		refreshSchedule();
	});
	
    $("#stopSolvingButton").click(function () {
        stopSolving();
    });
    
    $("#resetButton").click(function (){
	resetDatabase();
	});

    refreshSchedule();
});

function refreshSchedule() {
    $.getJSON("/programme/solve", function (result) {
        refreshSolvingButtons(result.solverStatus != null && result.solverStatus !== "NOT_SOLVING");
        $("#score").text("Score: " + (result.score == null ? "?" : result.score));	
        
    
        containerGroupDataSet.clear();
        containerItemDataSet.clear();
        
        $.each(result.prototypeList, (index, proto) => {
            containerGroupDataSet.add({id : proto.id, content: proto.name});
        });
        
        
        $.each(result.taskList, (index, job) => {
                const color = pickColor(job.sequence);
			    const beforeReady = JSJoda.LocalDate.parse(job.startDate).isBefore(JSJoda.LocalDate.parse(job.idealStartDate));
                const afterDue = JSJoda.LocalDate.parse(job.endDate).isAfter(JSJoda.LocalDate.parse(job.idealEndDate));
                const containerTaskElement = $(`<div />`)
                    .append($(`<h5 class="card-title mb-1" style="background-color: ${color}"/>`).text(job.name))
                    .append($(`<p class="card-text ml-2 mb-0"/>`).text(`${job.duration} workdays`));
                if (beforeReady) {
					const day = JSJoda.Period.between(JSJoda.LocalDate.parse(job.startDate),JSJoda.LocalDate.parse(job.idealStartDate));
                    containerTaskElement.append($(`<p class="badge badge-dark mb-0"/>`).text(`Tache en avance (${day})`));
                }
                if (afterDue) {
					const day = JSJoda.Period.between(JSJoda.LocalDate.parse(job.idealEndDate),JSJoda.LocalDate.parse(job.endDate));
                    containerTaskElement.append($(`<p class="badge badge-dark mb-0"/>`).text(`Tache en retard (${day})`));
                }
                
                containerItemDataSet.add({
                    id : job.id, group: job.prototype.id,
                    content: containerTaskElement.html(),
                    start: job.startDate, end: job.endDate
                });
	});
	
	containerTimeline.setWindow(result.workCalendar.fromDate, result.workCalendar.toDate);
	});
	
}

function refreshSolvingButtons(solving) {
    if (solving) {
        $("#solveButton").hide();
        $("#stopSolvingButton").show();
        if (autoRefreshIntervalId == null) {
            autoRefreshIntervalId = setInterval(refreshSchedule, 2000);
        }
    } else {
        $("#solveButton").show();
        $("#stopSolvingButton").hide();
        if (autoRefreshIntervalId != null) {
            clearInterval(autoRefreshIntervalId);
            autoRefreshIntervalId = null;
        }
    }
}

function stopSolving() {
    $.post("/programme/stopSolving", function () {
        refreshSolvingButtons(false);
        refreshSchedule();
    }).fail(function (xhr, ajaxOptions, thrownError) {
        showError("Stop solving failed.", xhr);
    });
}

function uplaodResource() {
	document.getElementById("resourceSpan").classList.add("fa-spin");
	document.getElementById("resourceButton").classList.remove("btn-info");
	document.getElementById("resourceButton").classList.add("btn-warning");
	document.getElementById("resourceSpanText").append("en cours...");
	if (document.getElementById("resourceFile").files.length == 0) {
		console.log("... Aucun fichier ...");
		document.getElementById("resourceSpan").classList.remove("fa-spin");
		document.getElementById("resourceSpanText").innerHTML = "Enregistrer";
		document.getElementById("resourceButton").classList.remove("btn-warning");
		document.getElementById("resourceButton").classList.add("btn-info");
		window.alert("aucun fichier");
	} else {
		let file = document.getElementById("resourceFile").files[0];
		let formData = new FormData();
		formData.append("file", file);
		var xhr = new XMLHttpRequest();
		xhr.open("POST", "/resource");
		xhr.send(formData);
		xhr.onreadystatechange = function() {
			if (xhr.readyState == 4)
				if (xhr.status == 200) {
					var json = JSON.parse(xhr.responseText);
					var divInfo = document.getElementById("resourceSize");
					divInfo.classList.add("alert");
					divInfo.classList.add("alert-info");
					divInfo.append("Vous venez de faire " + json.nombre + " Enregistrement");
					document.getElementById("resourceSpan").classList.remove("fa-spin");
					document.getElementById("resourceSpanText").innerHTML = "Enregistrer";
					document.getElementById("resourceButton").classList.remove("btn-warning");
					document.getElementById("resourceButton").classList.add("btn-info");
				}
		}
	}
}

function uploadHolidays() {
	if (document.getElementById("holidays").files.length == 0) {
		console.log("... Aucun fichier ...");
		window.alert("aucun fichier");
	} else {
		let file = document.getElementById("holidays").files[0];
		let formData = new FormData();
		formData.append("file", file);
		var xhr = new XMLHttpRequest();
		xhr.open("POST", "/holiday");
		xhr.send(formData);
		xhr.onreadystatechange = function() {
			if (xhr.readyState == 4)
				if (xhr.status == 200) {
					var json = JSON.parse(xhr.responseText);
					Swal.fire(
						'Stauts !',
						`<div class="alert alert-info"><p>Vous venez de faire ${json.nombre} Enregistrement</p></div>`,
						'success'
					  )
					divInfo.append("Vous venez de faire " + json.nombre + " Enregistrement");
				}
				else if (xhr.status == 404) {
					Swal.fire(
					  'Stauts !',
					  `<div class="alert alert-info"><p>Not found</p></div>`,
					  'error'
					)
				}
				else {
					Swal.fire(
					  'Stauts !',
					  `<div class="alert alert-info"><p>Il y'a une erreur de type ${xhr.status}</p></div>`,
					  'error'
					)}
		}
	}
}

function submitTeam(){
	
	const xValue = document.getElementById("xValue");
	const yValue = document.getElementById("yValue");
	const zValue = document.getElementById("zValue");
	
	var data = JSON.stringify({x:xValue.value, y: yValue.value, z:zValue.value});
	var xhr = new XMLHttpRequest();
	xhr.open("POST", "/team", true);
	xhr.setRequestHeader('Content-Type', 'application/json');
	xhr.send(data);
	xhr.onreadystatechange = function() {
		if (xhr.readyState == 4) {
			if (xhr.status == 200) {
				var json = JSON.parse(xhr.responseText);
				console.log(json);
				Swal.fire(
			      'Stauts !',
			      `<div class="alert alert-info"><p>${json.x}</p><p>${json.y}</p><p>${json.z}</p> </div>`,
			      'success'
			    )
			}
			else if (xhr.status == 404) {
				Swal.fire(
			      'Stauts !',
			      `<div class="alert alert-info"><p>Not found</p></div>`,
			      'error'
			    )
			}
			else {
				Swal.fire(
			      'Stauts !',
			      `<div class="alert alert-info"><p>Il y'a une erreur de type ${xhr.status}</p></div>`,
			      'error'
			    )}
		}
 	}
 }

function submitPrototype() {
	
	const protoName = document.getElementById("prototypeName");
	const protoDelivery = document.getElementById("prototypeDelivery");
	const type = document.getElementById('type').value;
	const phase = document.getElementById('phase').value;
	const moyen = document.getElementById('moyen').value;
	const factory = document.getElementById('factory').value;
	const date = new Date(protoDelivery.value)
	var data = JSON.stringify({ id: null, name: protoName.value, deliveryDate: date, phase: phase, type: type, moyen:moyen, usine: factory });
	console.log(data);
	var xhr = new XMLHttpRequest();
	xhr.open("POST", "/prototype", true);
	xhr.setRequestHeader('Content-Type', 'application/json');
	xhr.send(data);
	xhr.onreadystatechange = function() {
		if (xhr.readyState == 4) {
			if (xhr.status == 200) {
				var json = JSON.parse(xhr.responseText);
				console.log(json);
					Swal.fire(
				      `${json.prototype.name}`,
				      `<div class="alert alert-info"><p>${json.message}</p></div>`,
				      'success'
				    )
			}
			else if (xhr.status == 404) {
				Swal.fire(
			      'Stauts !',
			      `<div class="alert alert-info"><p>Not found</p></div>`,
			      'error'
			    )
			}
			else {Swal.fire('Stauts !',`<div class="alert alert-info"><p>Il y'a une erreur de type ${xhr.status}</p></div>`,'error')}
		}
	}
}

function loadPrototype() {
	var xhr = new XMLHttpRequest();
	xhr.open("GET", "/prototype", true);
	xhr.send();
	xhr.onreadystatechange = function() {
		if (xhr.readyState == 4) {
			if (xhr.status == 404) {
				const prototypeDiv = document.getElementById("prototypeDiv");
				prototypeDiv.innerHTML = "Not found";
			} if (xhr.status == 400 || xhr.status == 415) {
				const prototypeDiv = document.getElementById("prototypeDiv");
				prototypeDiv.innerHTML = "Votre requete est incorrect";
			} if (xhr.status == 200) {
				var json = JSON.parse(xhr.responseText);
				const prototypeDiv = document.getElementById("prototypeDiv");
				prototypeDiv.innerHTML =
					`<table class="table table-info table-striped">
					<thead>
					    <tr>
					      <th scope="col">Nom</th>
					      <th scope="col">Type</th>
					      <th scope="col">Phase</th>
					      <th scope="col">Date livraison</th>
					      <th scope="">Supprimer</th>
					    </tr>
					</thead>
			  		<tbody id="tbodyId"></tbody></table>`;

				$.each(json.prototypes, (index, prototype) => {
					const tr =
						`<tr>
					<td>${prototype.name}</td>
					<td>${prototype.type}</td>
					<td>${prototype.phase}</td>
					<td>${prototype.deliveryDate}</td>
					<td><button type="button" class="ml-2 mb-1 btn btn-light btn-sm p-1" onclick="deletePrototype(${prototype.id})" />
						<small class="fas fa-trash"/>
					</tr>`;
					document.getElementById("tbodyId").innerHTML += tr;
				})

			}
		}
	}
}

function solve() {
    $.post("/programme/solve", function() {
        refreshSolvingButtons(true);
    }).fail(function (xhr, ajaxOptions, thrownError) {
        showError("Start solving failed.", xhr);
    });
}

function deletePrototype(id) {
	let xhr = new XMLHttpRequest();
	xhr.open("DELETE", "/prototype/" + id, true);
	xhr.send();
	xhr.onreadystatechange = function() {
		if (xhr.readyState == 4)
			if (xhr.status == 200)
				loadPrototype();
	}
}

function resetDatabase(){
	Swal.fire({
	  title: 'Attention ?',
	  text: "Toute la base de données sera vidé",
	  icon: 'warning',
	  showCancelButton: true,
	  confirmButtonColor: '#3085d6',
	  cancelButtonColor: '#d33',
	  cancelButtonText:'Annuler',
	  confirmButtonText: 'Oui, réunitialisé !'
	}).then((result) => {
	  if (result.isConfirmed) {
		var xhr = new XMLHttpRequest();
	xhr.open("GET", "/prototype/delete", true);
	xhr.send();
	xhr.onreadystatechange = function() {
		if (xhr.readyState == 4) 
			if(xhr.status == 200){
				setTimeout(timer,2000);
				Swal.fire('Réunitialisé !','Votre base de donnée est vidé','success')
				}
		}
	  }
	  })}

function timer(){
	var button = document.getElementById("resetButton") ;
	button.innerHTML = "Reset";
	refreshSchedule();
}

function clearData(){
	var divInfo = document.getElementById("prototypeMsg");
	divInfo.classList.remove("alert");
	divInfo.classList.remove("alert-info");
	divInfo.innerHTML = "" ;
	 $('input[type="radio"]').prop('checked', false);
	$('input').each(function () {
		$(this).val('');
	});
}
// ****************************************************************************
// TangoColorFactory
// ****************************************************************************

const SEQUENCE_1 = [0x8AE234, 0xFCE94F, 0x729FCF, 0xE9B96E, 0xAD7FA8];
const SEQUENCE_2 = [0x73D216, 0xEDD400, 0x3465A4, 0xC17D11, 0x75507B];

var colorMap = new Map;
var nextColorCount = 0;

function pickColor(sequence) {
	const SEQUENCE_1 = [0xDE8F55, 0xF4D03F, 0x27AE60, 0xE9B96E, 0xEE41CE, 0x73D216, 0xEDD400, 0x3465A4, 0xEE4154, 0xEE41A5,0x41EEDE,0x9541EE,];
	let color;
    switch(sequence){
	case 1: color = SEQUENCE_1[0];  break;
	case 2: color = SEQUENCE_1[1];  break;
	case 3: color = SEQUENCE_1[2];  break;
	case 4: color = SEQUENCE_1[3];  break;
	case 5: color = SEQUENCE_1[4];  break;
	case 6: color = SEQUENCE_1[5];  break;
	case 7: color = SEQUENCE_1[6];  break;
	case 8: color = SEQUENCE_1[7];  break;
	case 9: color = SEQUENCE_1[8];  break;
	case 10: color = SEQUENCE_1[9];  break;
	case 11: color = SEQUENCE_1[10];  break;
	default: color = SEQUENCE_1[11];
}
    return "#" + color.toString(16);
}

function nextColor() {
    let color;
    let colorIndex = nextColorCount % SEQUENCE_1.length;
    let shadeIndex = Math.floor(nextColorCount / SEQUENCE_1.length);
    if (shadeIndex === 0) {
        color = SEQUENCE_1[colorIndex];
    } else if (shadeIndex === 1) {
        color = SEQUENCE_2[colorIndex];
    } else {
        shadeIndex -= 3;
        let floorColor = SEQUENCE_2[colorIndex];
        let ceilColor = SEQUENCE_1[colorIndex];
        let base = Math.floor((shadeIndex / 2) + 1);
        let divisor = 2;
        while (base >= divisor) {
            divisor *= 2;
        }
        base = (base * 2) - divisor + 1;
        let shadePercentage = base / divisor;
        color = buildPercentageColor(floorColor, ceilColor, shadePercentage);
    }
    nextColorCount++;
    return "#" + color.toString(16);
}

function buildPercentageColor(floorColor, ceilColor, shadePercentage) {
    let red = (floorColor & 0xFF0000) + Math.floor(shadePercentage * ((ceilColor & 0xFF0000) - (floorColor & 0xFF0000))) & 0xFF0000;
    let green = (floorColor & 0x00FF00) + Math.floor(shadePercentage * ((ceilColor & 0x00FF00) - (floorColor & 0x00FF00))) & 0x00FF00;
    let blue = (floorColor & 0x0000FF) + Math.floor(shadePercentage * ((ceilColor & 0x0000FF) - (floorColor & 0x0000FF))) & 0x0000FF;
    return red | green | blue;
}