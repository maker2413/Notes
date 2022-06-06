window.onload = function() {
  // Get the query
  const query = new URLSearchParams(window.location.search).get("query");

  console.log(query);
  console.log(document);
  console.log(document.getElementById('search-input'));

  // Perform a search if there is a query
  if (query) {
    document.getElementById("search-input").setAttribute("value", query);

    console.log(window);
    console.log(window.store);
  }
};
