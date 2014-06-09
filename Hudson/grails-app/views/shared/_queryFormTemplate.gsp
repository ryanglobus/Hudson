<%@ page import="hudson.Query" %>
<%@ page import="hudson.neighborhood.*" %>
<%@ page import="java.util.StringTokenizer" %>
<%-- Expects a model with key 'isNew' of type boolean and 'query' of type Query,
which is not null (if new, simply use new Query()). profile.css, profile.js,
bootstrap-multiselect.css, and bootstrap-multiselect.js should be included. --%>

<%
def titlize = { String str ->
    String title = ''
    StringTokenizer tokenizer = new StringTokenizer(str)
    boolean first = true
    while (tokenizer.hasMoreTokens()) {
        String token = tokenizer.nextToken()
        if (first) first = false
        else title += ' '
        title += token.capitalize()
    }
    return title
}
%>

<script>
if (typeof(initialNeighborhoods) === 'undefined') {
    initialNeighborhoods = {}
    jQuery(function() {
    	var $ = jQuery

    	$('[data-toggle=tooltip]').tooltip()
    })
} 
</script>

<g:form useToken="true" class="query-form form-horizontal" controller="profile" action="${isNew ? 'newquery' : 'editQuery'}" role="form" data-query-id="${query.id}">
	<div class="category">
        <h4 class="center-block">General</h4>
        <div class="form-group">
            <label for="queryName" class="control-label col-sm-2">Query Name:</label>
            <div class="col-sm-9">
                <g:field type="text" name="queryName" class="form-control" placeholder="Query Name" value="${query.name}" />
            </div>
        </div>
        
        <% checkboxes = [['notify', 'Receive Notifications:', isNew || query.notify],
                        ['instantReply', 'Auto-Reply:', query.instantReply]] %>
        <% for (def checkbox : checkboxes) { %>
            <div class="form-group">
				<div class="col-sm-2">
	                <label class="control-label" for="${checkbox[0]}">${checkbox[1]}</label> <br />
					<% if (checkbox[1] == 'Receive Notifications:') { %>
						<span class="glyphicon glyphicon-question-sign" data-toggle="tooltip" data-placement="bottom" title="Select to recieve notifications"></span>
	                <% } %>
					<% if (checkbox[1] == 'Auto-Reply:') { %>
						<span class="glyphicon glyphicon-question-sign" data-toggle="tooltip" data-placement="bottom" title="Select to have Hudson automatically reply to new posts"></span>
					<% } %>
				</div>
				<div class="col-sm-2">
                    <g:checkBox name="${checkbox[0]}" class="form-control" value="${checkbox[2]}" />
                </div>
            </div>
        <% } %>
        <textArea rows="6" cols="60" name="responseMessage" id="responseBox" style="${query.instantReply ? '' : 'display:none'}" placeholder ="Insert a Response Message Here!">${query.responseMessage}</textArea>
    </div>
    <br>

    <div class="category">
        <h4>Location</h4>
        <div class="form-group">
            <label for="region" class="col-sm-2 control-label">Region:</label>
            <div class="col-sm-9">
                <select class="form-control col-sm-9" name="region">
                    <% Region region = Region.sfbay() %>
                    <option value="${region.value}">${titlize(region.name)}</option>
                </select>
            </div>
        </div>
        
        <div class="form-group">
            <label for="city" class="col-sm-2 control-label">City:</label>
            <div class="col-sm-9">
                <select class="form-control" name="city">
                    <option value="">All cities</option>
                    <% for (City city : region.cities) { %>
                        <option value="${city.value}" ${(!isNew && query.city?.value == city.value) ? 'selected="selected"' : ''}>${titlize(city.name)}</option>
                    <% } %>
                </select>
            </div>
        </div>
        <div class="form-group">
            <label for="neighborhoods" class="col-sm-2 control-label">Neighborhood(s):</label>
            <div class="col-sm-1">
                <select class="form-control multiselect" name="neighborhoods" multiple disabled>
                </select>
                <% if (!isNew) { %>
                    <script>
                    initialNeighborhoods['${query.id}'] = [
                    <% for (Neighborhood nh : query.neighborhoods) { %>
                        "${nh.value}",
                    <% } %>
                    ];
                    </script>
                <% } %>
            </div>
        </div>
    </div>

	<br>
    <div class="category">
        <h4>Housing Preferences</h4>
        <div class="form-group">
            <label for="numRooms" class="col-sm-2 control-label"># Bedrooms:</label>
            <div class="col-sm-9"> 
                <g:field type="number" class="form-control" name="numRooms" placeholder="# BRs" value="${query.numBedrooms}" />
            </div>
        </div>
        <div class="form-group">
            <label class="control-label col-sm-2">Rent:</label>
            <div class="col-sm-9">
                <label for="minrent" class="sr-only control-label">Minimum:</label>
                <g:field type="number" class="form-control col-sm-2" name="minrent" placeholder="Minimum" value="${query.minRent}" />
                <label class="control-label col-sm-1" style="text-align:center">to</label>
                <label for="maxrent" class="sr-only control-label">Maximum:</label>
                <g:field type="number" class="form-control col-sm-2" name="maxrent" placeholder="Maximum" value="${query.maxRent}"/>
            </div>
        </div>
        <div class="form-group">
            <label class="control-label col-sm-2" for="type">Type:</label>
            <div class="col-sm-9">
                <select class="form-control" name = "type" id="type">
                    <g:each var="housingType" in="${Query.HousingType.values()}">
                        <option value="${housingType.name()}" ${(!isNew && query.housingType == housingType.getValue()) ? 'selected="selected"' : ''}>${housingType.userFriendlyName()}</option>
                    </g:each>
                </select>
            </div>
        </div>
        <div class="form-group">
            <label class="control-label col-sm-2">Pets:</label>
            <div class="col-sm-2" style="text-align:left">
                <% def checkboxes = [['cat', 'Cats', query.cat],
                                    ['dog', 'Dogs', query.dog]] %>
                <% for (def checkbox : checkboxes) { %>
                    <g:checkBox name="${checkbox[0]}" value="${checkbox[2]}" />
                    <label for="${checkbox[0]}">${checkbox[1]}</label>
                <% } %>
            </div>
        </div>
        <div class="form-group">
            <label for="searchText" class="col-sm-2 control-label">Keywords:</label>
            <div class="col-sm-9">
                <g:field type="text" name="searchText" class="form-control" placeholder="Search" value="${query.searchText}" />
            </div>
        </div>
    </div>
    
    <g:if test="isNew">
        <input type="hidden" name="qryId" value="${query.id}" />
    </g:if>
    <input class="btn btn-primary center-block" type= "submit" value="<%= isNew ? 'Create Search!' : 'Edit Search' %>"/>
</g:form>

