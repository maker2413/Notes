'use strict';

let notes = [window.location.pathname];
// console.log(notes);
// console.log(window.location);

// function createContainer(link, html) {
//   console.log(Number(link.dataset.level));
// }

function initializePage(note) {
  let links = Array.prototype.slice.call(note.querySelectorAll('a'));

  links.forEach(async function (el) {
    // let linkFile = el.getAttribute('href');
    let link = el.href;

    let response = await fetch(link);
    let template = document.createElement('template');

    template.innerHTML = await response.text();
    console.log(template.content.querySelector('.node').outerHTML);

    createContainer(el, template.content.querySelector('.node').outerHTML);
  })
}

window.onload = function() {
  initializePage(document.querySelector('.note'));
};
