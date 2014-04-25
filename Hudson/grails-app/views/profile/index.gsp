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
						<option value = "any housing type">Any</option>
						<option value = "apartment">Apartment</option>
						<option value = "condo">Condo</option>
						<option value = "cottage/cabin">Cottage/Cabin</option>
						<option value = "duplex">Duplex</option>
						<option value = "flat">Flat</option>
						<option value = "house">House</option>
						<option value = "in-law">In-Law</option>
						<option value = "loft">Loft</option>
						<option value = "townhouse">Townhouse</option>
						<option value = "manufactured">Manufactured</option>
						<option value = "assisted">Assisted Living</option>
						<option value = "land">Land</option>
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