'use strict';

let pages = [window.location.pathname];
// console.log(notes);
// console.log(window.location);

function stackPage(link, level) {
  level = Number(level) || pages.length;
  link = URI(link);
  let url = URI(window.location);
  pages.push(link.path());
  url.setQuery('stackedNotes', pages.slice(1, pages.length));

  let oldPages = pages.slice(0, level - 1);
  let state = { pages: oldPages, level: level };
  window.history.pushState(state, '', url.href());
}

function unstackPages(level) {
  let container = document.querySelector('.notes');
  let children = Array.prototype.slice.call(container.children);

  for (let i = level; i < children.length; i++) {
    container.removeChild(children[i]);
  }
  pages = pages.slice(0, level);
}

function updateLinkStatuses() {
  let links = Array.prototype.slice.call(document.querySelectorAll("a"));
  links.forEach(function (e) {
    if (pages.indexOf(e.getAttribute("href")) > -1) {
      e.classList.add("active");
    } else {
      e.classList.remove("active");
    }
  });
}

function fetchPage(link, level) {
  if (pages.indexOf(link) > -1) return;
  level = Number(level) || pages.length;

  const request = new Request(link);
  fetch(request)
    .then((response) => response.text())
    .then((text) => {
      unstackPages(level);
      let container = document.querySelector('.notes');
      let template = document.createElement('template');
      template.innerHTML = text;
      let el = template.content.querySelector('.note');
      container.appendChild(el);
      stackPage(link, level);
    });
}

function initializePage(note, level) {
  level = level || pages.length;

  let links = Array.prototype.slice.call(note.querySelectorAll('a'));

  links.forEach(async function (el) {
    let rawHref = el.getAttribute('href');
    el.dataset.level = level;

    if (
      rawHref && !(
        (
          rawHref.indexOf('http://') === 0 ||
          rawHref.indexOf('https://') === 0 ||
          rawHref.indexOf('#') === 0
        )
      )
    ) {
      let link = el.href;

      let response = await fetch(link);
      let template = document.createElement('template');

      template.innerHTML = await response.text();
      if (response.headers.get('content-type').includes('text/html')) {
        el.addEventListener('click', function (e) {
          if (!e.ctrlKey && !e.metaKey) {
            e.preventDefault();
            fetchPage(el.getAttribute('href'), this.dataset.level);
          }
        });
      }
      updateLinkStatuses();
    }
    // console.log(template.content.querySelector('.note').outerHTML);
  })
}

window.addEventListener("popstate", function (event) {
  // TODO: check state and pop pages if possible, rather than reloading.
  window.location = window.location; // this reloads the page.
});

window.onload = function() {
  initializePage(document.querySelector('.note'));

  let url = URI(window.location);
  if (url.hasQuery('stackedNotes')) {
    let stacks = url.query(true).stackedNotes;
    if (!Array.isArray(stacks)) {
      stacks = [stacks];
    }
    for (let i = 0; i < stacks.length; i++) {
      fetchPage(stacks[i], i + 1);
    }
  }
};
