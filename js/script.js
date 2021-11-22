'use strict';

let pages = [window.location.pathname];
let switchDirectionWindowWidth = 900;
let animationLength = 200;

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
  let links = Array.prototype.slice.call(document.querySelectorAll('a'));

  links.forEach(function (e) {
    if (pages.indexOf(e.getAttribute('href')) > -1) {
      e.classList.add('active');
    } else {
      e.classList.remove('active');
    }
  });
}

function fetchPage(link, dir, level) {
  if (pages.indexOf(link) > -1) return;

  level = Number(level) || pages.length;

  console.log(dir);
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

      setTimeout(
        function (el, dir, level) {
          el.dataset.level = level + 1;
          el.dataset.dir = dir;
          initializePage(el, dir, level + 1);
          el.scrollIntoView();
          if (window.MathJax) {
            window.MathJax.typeset();
          }
        }.bind(null, el, dir, level),
        10
      );
    });
}

function initializePage(note, dir, level) {
  level = level || pages.length;

  let links = Array.prototype.slice.call(note.querySelectorAll('a'));

  links.forEach(async function (el) {
    let rawHref = el.getAttribute('href');
    el.dataset.level = level;
    el.dataset.dir = dir;

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

      let testLink = new URL(link);

      console.log(el.dataset.dir, testLink.pathname);
      if (!(dir === '/')) {
        if (!(testLink.pathname.indexOf(el.dataset.dir) === 0)) {
          testLink.pathname =
            el.dataset.dir +
            testLink.pathname.split(el.dataset.dir).pop();
          console.log('we got em!', pages[0], el.dataset.dir, testLink.pathname);
        } else {
          console.log(testLink.pathname.split(el.dataset.dir).pop());
          testLink.pathname =
            el.dataset.dir +
            testLink.pathname.split(el.dataset.dir).pop();
        }
      }
      // console.log(el.dataset.dir, testLink.pathname);
      // console.log(testLink.pathname + testLink.hash, el.getAttribute('href'));

      el.dataset.dir = el.dataset.dir + testLink.pathname.substring(1, testLink.pathname.lastIndexOf('/') + 1);
      console.log(el.dataset.dir, testLink.pathname);

      // console.log(link.substring(link.split('/', 3).join('/').length + 1, link.lastIndexOf('/')));

      let response = await fetch(testLink);
      let template = document.createElement('template');

      template.innerHTML = await response.text();
      if (response.headers.get('content-type').includes('text/html')) {
        el.addEventListener('click', function (e) {
          if (!e.ctrlKey && !e.metaKey) {
            e.preventDefault();
            fetchPage(testLink.pathname + testLink.hash,
                      this.dataset.dir,
                      this.dataset.level
                     );
          }
        });
      }

      updateLinkStatuses();
    }
  })
  console.log('---------------------------------------');
}

window.addEventListener('popstate', function (event) {
  // TODO: check state and pop pages if possible, rather than reloading.
  window.location = window.location; // this reloads the page.
});

window.onload = function() {
  initializePage(document.querySelector('.note'), '/');

  let url = URI(window.location);

  if (url.hasQuery('stackedNotes')) {
    let stacks = url.query(true).stackedNotes;
    if (!Array.isArray(stacks)) {
      stacks = [stacks];
    }
    for (let i = 0; i < stacks.length; i++) {
      fetchPage(stacks[i], '/', i + 1);
    }
  }
};
