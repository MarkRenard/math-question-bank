// This script provides functionality to otherwise hidden assignment buttons

$(document).ready(function(){
	
	// Prepares form elements when the user selects an assignment category
	$('select[name="assignment-category"]').change(function(){
		var cid = $(this).val();
		
		// Sets category id in hidden form element
		$("#assignment-cid").val(cid);
		window.alert("Cid set: " + cid);
		
		prepareAssignmentButtons(cid);
		
	})
	
	// Shows only elements in the selected display category
//	$('select[name="display-category"]').change(function(){
//		var cid = $(this).val();
//		
//		showIffInCategory(".problem-row", cid, "block");
//		
//	})
		
	// Shows each assignment button not in the selected category, hides others
	function prepareAssignmentButtons(cid){
		hideIffNotInCategory(".assign-button", cid);
		hideIffNotInCategory(".assign-glyphicon", cid);
		showIffInCategory(".added-glyphicon", cid);
	}
	
	// Hides elements of class elementClass with identical cid, shows others
	function hideIffNotInCategory(elementClass, cid, display){
		//Default display parameter
		var display = (typeof display !== 'undefined') ? display : "inline";
		
		$(elementClass).each(function(){
			if ($(this).attr("cid") != cid){
				$(this).attr("style", "display: " + display);
			} else {
				$(this).attr("style", "display: none");
			}
		})
	}
	
	// Shows elements of class elementClass with identical cid, hides others
	function showIffInCategory(elementClass, cid, display){
		
		//Default display parameter
		var display = (typeof display !== 'undefined') ? display : "inline";
		
		$(elementClass).each(function(){
			if ($(this).attr("cid") == cid){
				$(this).attr("style", "display: " + display);
			} else {
				$(this).attr("style", "display: none");
			}
		})
	}
	
	// Sets the problem id when a category assignment button is clicked
	$(".assign-button").click(function(){
		var pid = $(this).attr("pid");
		
		// Sets problem id in hidden form element
		$("#assignment-pid").val(pid);
		window.alert("Assignment pid set: " + pid)
		
		$(this).attr("type", "submit");
	})
})