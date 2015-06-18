<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>


<script type="text/javascript">
	
	
$("document").ready(function(){
	var unsavedNewChanges = false;
	
	$("#addButton").button();
	$("#addButton").button("enable");
	$("#saveButton").button();
	$("#saveButton").button("enable");
	

	var companyDataTable = $("#companyDataTable").DataTable({
		"bJQueryUI" : true,
		"bSortClasses" : false,
		"filter" : false,
		"info" : false,
		"paginate" : false,
		"columnDefs" : [{"visible" : false, "targets" : [0]}],
		"dom" : 'rti',
		"serverSide" : false,
		"processing" : true,
		"serverParams" : function(aoData){
			//aoData.push({"name":"--name--", "value": $("#value").val()});
		},
		"ajaxSource" : "loadCompanyTable",
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
			$(nRow).data("company", aData[1]);
		},
		"initComplete" : function(){
			
		}
	});		
	
	$("#companyDataTable tbody").on("click", "tr", function(e){
		$(this).toggleClass("selected");
	});
	
	$("#addButton").click(function(){
		if($("#newCompany").length){
			unsavedNewChanges = true;
			alert("Please save new company before adding another");
		}
		else{
			companyDataTable.row.add(["", "<input type='text' name='newCompany' id='newCompany' class='text ui-widget-content ui-corner-all'></input>", "<input type='text' name='newPhone' id='newPhone' class='text ui-widget-content ui-corner-all'></input>", "<input type='text' name='newEmail' id='newEmail' class='text ui-widget-content ui-corner-all'></input>"]).draw();
		}
	});
	
	$("#saveButton").click(function(){
		if($("#newCompany").val() != "" && $("#newCompany").val() != null){
			$.ajax({
				url:"saveNewCompany",
				datatype:"json",
				data:{
					"companyName" : $("#newCompany").val(),
					"companyPhone" : $("#newPhone").val(),
					"companyEmail" : $("#newEmail").val()
				},
				success:function(str){
					if(str=="success"){
						companyDataTable.ajax.reload();
						unsavedNewChanges = false;
					}
					else{
						alert("Unable to save company: " + str);
					}
				}, 
				error: function(jqXHR, statusText, errorThrown){
					alert("Error loading table data: " + statusText);
				}
				
			});
		}
		else{
			alert("Cannot save a blank company");
		}
	});
	
	window.onbeforeunload = function(){
		if(unsavedNewChanges){
			return 'Are you sure you want to leave?';
		}
	};
});
	
	
</script>

<div id="buttonRow" style="margin-top:50px;margin-left:25px;">
	<a href="javascript:void(0)" id="addButton">Add Company</a>
	<a href="javascript:void(0)" id="saveButton">Save</a>
</div>

<div style="margin:20px;">
	<table id="companyDataTable" width="50%">
		<thead>
			<tr>
				<th>id-HIDDEN</th>
				<th>Company</th>
				<th>Phone</th>
				<th>Email</th>
			</tr>
		</thead>
		<tbody class="ajaxDataTable"></tbody>
	</table>
</div>
