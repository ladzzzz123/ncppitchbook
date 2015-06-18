<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>


<script type="text/javascript">
	
	
$("document").ready(function(){
	var unsavedNewChanges = false;
	
	$(".ncpButton").button();
	$(".ncpButton").button("enable");
	$("#convertButton").button("disable");
	

	var userDataTable = $("#userDataTable").DataTable({
		"bJQueryUI" : true,
		"bSortClasses" : false,
		"filter" : false,
		"stripeClasses": [ 'strip1', 'strip2', 'strip3' ],
		"info" : false,
		"paginate" : true,
		"columnDefs" : [{"visible" : false, "targets" : [0]}],
		"dom" : 'rti',
		"serverSide" : false,
		"processing" : true,
		"serverParams" : function(aoData){
			//aoData.push({"name":"--name--", "value": $("#value").val()});
		},
		"ajaxSource" : "loadUserTable",
		"serverData" : function(sSource, aoData, callback){
			$.ajax({
				dataType : "json", 
				url : sSource,
				data : aoData,
				cache : false,
				success : function(json){
					callback(json);
					
				},
				error : function(jqXHR, statusText, errorThrown){
					alert("Error loading table data: " + statusText);
				}
			});
		},
		"drawCallback" : function(oSettings){
			
		},
		"rowCallback" : function(nRow, aData, iDisplayIndex, iDisplayIndexFull){
			$(nRow).data("id", aData[0]);
			$(nRow).data("username", aData[1]);
			$(nRow).data("password", aData[2]);
			$(nRow).data("firstName", aData[3]);
			$(nRow).data("lastName", aData[4]);
			$(nRow).data("emailAddress", aData[5]);
			$(nRow).data("emailPassword", aData[6]);
		},
		"initComplete" : function(){
			
		}
	});		
	
	$("#userDataTable tbody").on("click", "tr", function(e){
		if($(this).hasClass("selected")){
			$(this).removeClass("selected");
			$("#convertButton").button("disable");
		}
		else{
			$(".selected").removeClass("selected");
			$(this).addClass("selected");
			$("#convertButton").button("enable");
		}
	});
	
	$("#addButton").click(function(){
		if($("#newUser").length){
			unsavedNewChanges = true;
			alert("Please save new user before adding another");
		}
		else{
			userDataTable.row.add(["", "<input type='text' name='newUsername' id='newUsername' class='text ui-widget-content ui-corner-all'></input>",
			                       "<input type='password' name='newPassword' id='newPassword' class='text ui-widget-content ui-corner-all'></input>",
			                       "<input type='text' name='newFirstName' id='newFirstName' class='text ui-widget-content ui-corner-all'></input>",
			                       "<input type='text' name='newLastName' id='newLastName' class='text ui-widget-content ui-corner-all'></input>",
			                       "<input type='text' name='newEmailAddress' id='newEmailAddress' class='text ui-widget-content ui-corner-all'></input>",
			                       "<input type='password' name='newEmailPassword' id='newEmailPassword' class='text ui-widget-content ui-corner-all'></input>"]).draw();
		}
	});
	
	$("#saveButton").click(function(){
		if($("#newUsername").val() != "" && $("#newUsername").val() != null && 
				$("#newPassword").val() != "" && $("#newPassword").val() != null && 
				$("#newFirstName").val() != "" && $("#newFirstName").val() != null&& 
				$("#newLastName").val() != "" && $("#newLastName").val() != null&& 
				$("#newEmailAddress").val() != "" && $("#newEmailAddress").val() != null&& 
				$("#newEmailPassword").val() != "" && $("#newEmailPassword").val() != null){
			$.ajax({
				url:"saveNewUser",
				datatype:"json",
				data:{
					"username" : $("#newUsername").val(),
					"password" : $("#newPassword").val(),
					"firstName" : $("#newFirstName").val(),
					"lastname" : $("#newLastname").val(),
					"emailAddress" : $("#newEmailAddress").val(),
					"emailPassword" : $("#newEmailPassword").val()
				},
				success:function(str){
					if(str=="success"){
						userDataTable.ajax.reload();
						unsavedNewChanges = false;
					}
					else{
						alert("Unable to save user: " + str);
					}
				}, 
				error: function(jqXHR, statusText, errorThrown){
					alert("Error loading table data: " + statusText);
				}
				
			});
		}
		else{
			alert("Cannot save a blank user");
		}
	});
	
	$("#convertButton").click(function(){
		$.ajax({
			url:"runConversionForUser",
			datatype:"json",
			data:{
				"userId" : $("#userDataTable tr.selected").data("id")
			},
			success:function(str){
				if(str=="success"){
					alert("running conversion");
				}
				else{
					alert("Unable to convert for user: " + str);
				}
			}, 
			error: function(jqXHR, statusText, errorThrown){
				alert("Error loading table data: " + statusText);
			}
		});
	});
	
	window.onbeforeunload = function(){
		if(unsavedNewChanges){
			return 'Are you sure you want to leave?';
		}
	};
});
	
	
</script>

<div id="buttonRow" style="margin-top:50px;margin-left:25px;">
	<a href="javascript:void(0)" class="ncpButton" id="addButton">Add User</a>
	<a href="javascript:void(0)" class="ncpButton" id="saveButton">Save</a>
	<a href="javascript:void(0)" class="ncpButton" id="convertButton" style="float:right;">Run Conversion For User</a>
</div>

<div style="margin:20px;">
	<table id="userDataTable" width="50%" class="selectable">
		<thead>
			<tr>
				<th>id-HIDDEN</th>
				<th>Username</th>
				<th>Password</th>
				<th>First Name</th>
				<th>Last Name</th>
				<th>Email</th>
				<th>Email Password</th>
			</tr>
		</thead>
		<tbody class="ajaxDataTable"></tbody>
	</table>
</div>
