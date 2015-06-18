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
		

		var recordDataTable = $("#recordDataTable").DataTable({
			"bJQueryUI" : true,
			"bSortClasses" : false,
			"filter" : false,
			"info" : false,
			"paginate" : false,
			"columnDefs" : [{"visible" : false, "targets" : [0,8]}],
			"sDom": 'T<"clear">Clfrtip<"clear spacer">',
            "tableTools" : {
		        "sSwfPath": "../resources/swf/copy_csv_xls_pdf.swf",
		        "aButtons": [
		        {
		            "sExtends": "copy",
		            //could also have array of visible columns if don't want all visible i.e. [0,2,4,5,6]
		            "mColumns": "visible"
		        },
		        {
		            "sExtends": "xls",
		            "mColumns": "visible"
		        },
		        {
		            "sExtends": "pdf",
		            "mColumns": "visible",
		            "sPdfOrientation": "landscape"
		        },
		        {
		            "sExtends": "print",
		            "mColumns": "visible"
		        }
		        ]
		    },
		            "colVis": {
		        exclude: [ 0,1 ],
		        "showAll": "Show all",
		        "showNone": "Show none"
		    },
			"serverSide" : false,
			"processing" : true,
			"serverParams" : function(aoData){
				//aoData.push({"name":"--name--", "value": $("#value").val()});
			},
			"ajaxSource" : "loadRecordsTable",
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
				$(nRow).data("record", aData[1]);
			},
			"initComplete" : function(){
				
			}
		});		
		
		$("#recordDataTable tbody").on("click", "tr", function(e){
			$(this).toggleClass("selected");
		});
		
		$("#addButton").click(function(){
			if($("#newRecord").length){
				unsavedNewChanges = true;
				alert("Please save new record before adding another");
			}
			else{
				recordDataTable.row.add(["", "<input type='text' name='newRecord' id='newRecord' class='text ui-widget-content ui-corner-all'></input>"]).draw();
			}
		});
		
		$("#saveButton").click(function(){
			if($("#newRecord").val() != "" && $("#newRecord").val() != null){
				$.ajax({
					url:"saveNewRecord",
					datatype:"json",
					data:{
						"record" : $("#newRecord").val()
					},
					success:function(str){
						if(str=="success"){
							recordDataTable.ajax.reload();
							unsavedNewChanges = false;
						}
						else{
							alert("Unable to save record: " + str);
						}
					}, 
					error: function(jqXHR, statusText, errorThrown){
						alert("Error loading table data: " + statusText);
					}
					
				});
			}
			else{
				alert("Cannot save a blank record");
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
		<a href="javascript:void(0)" id="addButton">Add Category</a>
		<a href="javascript:void(0)" id="saveButton">Save</a>
	</div>

	<div style="margin:20px;">
		<table id="recordDataTable" width="50%">
			<thead>
				<tr>
					<th>id-HIDDEN</th>
					<th>Category</th>
					<th>Industry</th>
					<th>Company</th>
					<th>Value</th>
					<th>City</th>
					<th>State</th>
					<th>Country</th>
					<th>Description</th>
					<c:forEach items="${types }" var="type">
						<c:if test="${type ne 'Main Company'}">
						   <th>${type }</th>
						</c:if>
						
					</c:forEach>
				</tr>
			</thead>
			<tbody class="ajaxDataTable"></tbody>
		</table>
	</div>