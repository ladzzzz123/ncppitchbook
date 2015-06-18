<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ page session="false" %>

<spring:url value="/resources" var="resourceUrl"/>

<script>
	
	
$("document").ready(function(){

var unsavedNewChanges = false;
	
	$("#addButton").button();
	$("#addButton").button("enable");
	$("#saveButton").button();
	$("#saveButton").button("enable");
	

	var industryDataTable = $("#industryDataTable").DataTable({
		"bJQueryUI" : true,
		"bSortClasses" : false,
		"filter" : false,
		"info" : false,
		"paginate" : true,
		"columnDefs" : [{"visible" : false, "targets" : [0]}],
		"dom" : 'rti',
		"serverSide" : false,
		"processing" : true,
		"serverParams" : function(aoData){
			//aoData.push({"name":"--name--", "value": $("#value").val()});
		},
		"ajaxSource" : "loadIndustryTable",
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
			$(nRow).data("industry", aData[1]);
		},
		"initComplete" : function(){
			
		}
	});		
	
	$("#industryDataTable tbody").on("click", "tr", function(e){
		$(this).toggleClass("selected");
	});
	
	$("#addButton").click(function(){
		if($("#newIndustry").length){
			unsavedNewChanges = true;
			alert("Please save new industry before adding another");
		}
		else{
			industryDataTable.row.add(["", "<input type='text' name='newIndustry' id='newIndustry' class='text ui-widget-content ui-corner-all'></input>"]).draw();
		}
	});
	
	$("#saveButton").click(function(){
		if($("#newIndustry").val() != "" && $("#newIndustry").val() != null){
			$.ajax({
				url:"saveNewIndustry",
				datatype:"json",
				data:{
					"industry" : $("#newIndustry").val()
				},
				success:function(str){
					if(str=="success"){
						industryDataTable.ajax.reload();
						unsavedNewChanges = false;
					}
					else{
						alert("Unable to save industry: " + str);
					}
				}, 
				error: function(jqXHR, statusText, errorThrown){
					alert("Error loading table data: " + statusText);
				}
				
			});
		}
		else{
			alert("Cannot save a blank industry");
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
	<a href="javascript:void(0)" id="addButton">Add Industry</a>
	<a href="javascript:void(0)" id="saveButton">Save</a>
</div>

<div style="margin:20px;">
	<table id="industryDataTable" width="50%">
		<thead>
			<tr>
				<th>id-HIDDEN</th>
				<th>Industry</th>
			</tr>
		</thead>
		<tbody class="ajaxDataTable"></tbody>
	</table>
</div>
