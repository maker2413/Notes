'use strict';

let notes = [window.location.pathname];
// console.log(notes);
// console.log(window.location);

function createContainer() {
}

function initializePage(note) {
  let links = Array.prototype.slice.call(note.querySelectorAll('a'));
  // console.log(links);

  links.forEach(async function (el) {
    // let linkFile = el.getAttribute('href');
    let link = el.href;
    // console.log(linkFile, link);

    let response = await fetch(link);
    let template = document.createElement('template');
    console.log(response, template);

    template.innerHTML = await response.text();
    console.log(template);

    createContainer(el, );

    // async function find() {
    //   let response = await fetch(link);
    //   console.log(response);
    // }
    // return find();
  })
}

window.onload = function() {
  // console.log(document.querySelector(".note"));

  initializePage(document.querySelector('.note'));
};
