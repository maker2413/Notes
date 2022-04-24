// Get the query
const query = new URLSearchParams(window.location.search).get("query");

// Perform a search if there is a query
if (query) {
  document.getElementById("search-input").setAttribute("value", query);
}
