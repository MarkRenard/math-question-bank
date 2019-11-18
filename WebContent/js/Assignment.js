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
		
	// Shows each assignment button not in the selected category, hides others
	function prepareAssignmentButtons(cid){
		hideIffInCategory(".assign-button", cid);
		hideIffInCategory(".assign-glyphicon", cid);
		showIffInCategory(".added-glyphicon", cid);
	}
	
	// Hides elements of class identifier with identical cid, shows others
	function hideIffInCategory(identifier, cid){
		$(identifier).each(function(){
			if ($(this).attr("cid") == cid){
				$(this).attr("style", "display: none");
			} else {
				$(this).attr("style", "display: inline");
			}
		})
	}
	
	// Shows elements of class identifier with identical cid, hides others
	function showIffInCategory(identifier, cid){
		$(identifier).each(function(){
			if ($(this).attr("cid") == cid){
				$(this).attr("style", "display: inline");
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