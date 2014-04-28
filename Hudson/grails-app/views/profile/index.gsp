<!DOCTYPE html>
<html>
	
	<head>
		<title>New Query</title>
	</head>
	
	<body>
		<g:form action = "newquery">
			<div style = "width:350px">
				<label>Search:</label> <input type= "text" name= "searchText"/>
				<label>Minimum:</label> <input type= "number" name= "minrent"/>
				<label>Maximum:</label> <input type= "number" name= "maxrent"/>
				<label>Number Of Bedrooms:</label> <input type = "number" name= "numRooms"/>
				<label for="type">Style:</label> 
					<select name = "type" id="type">
						<option value = "APARTMENT">Apartment</option>
						<option value = "CONDO">Condo</option>
						<option value = "COTTAGE_CABIN">Cottage/Cabin</option>
						<option value = "DUPLEX">Duplex</option>
						<option value = "FLAT">Flat</option>
						<option value = "HOUSE">House</option>
						<option value = "IN_LAW">In-Law</option>
						<option value = "LOFT">Loft</option>
						<option value = "TOWNHOUSE">Townhouse</option>
						<option value = "MANUFACTURED">Manufactured</option>
						<option value = "ASSISTED_LIVING">Assisted Living</option>
						<option value = "LAND">Land</option>
					</select>
				<input type = "checkbox" name ="cat" id="cat" />
				<label for="cat">Cat</label>
				
				<input type="checkbox" name="dog" id="dog" />
				<label for="dog">Dog</label>
				<input type="checkbox" name="notify" id="notify"/><label for="notify">Email notifications?</label>
				<input type="checkbox" name="instantReply" id="instantReply"/>
				<label for="instantReply">Would you like Hudson to send an automatic reply?</label>
				<input type = "text" name="responseMessage">
					
				<label>&nbsp;</label><input type= "submit" value="newquery"/>
			</div>
		</g:form>
		
	</body>	
</html>