'use strict';

let pages = [window.location.pathname];
let switchDirectionWindowWidth = 900;
let animationLength = 800;

function stackPage(link, level) {
  level = Number(level) || pages.length;
  link = URI(link);
  let url = URI(window.location);
  pages.push(link.path());
  url.setQuery('stack', pages.slice(1, pages.length));

  let oldPages = pages.slice(0, level - 1);
  let state = { pages: oldPages, level: level };
  window.history.pushState(state, '', url.href());
}

function unstackPages(level) {
  let container = document.querySelector('.notes');
  let children = Array.prototype.slice.call(container.children);

  for (let i = level; i < children.length; i++) {
    container.removeChild(children[i]);
    destroyPreviews(children[i]);
  }
  pages = pages.slice(0, level);
}

function destroyPreviews(note) {
  let links = Array.prototype.slice.call(note.querySelectorAll("a[data-uuid]"));
  links.forEach(function (link) {
    if (link.hasOwnProperty("_tippy")) {
      link._tippy.destroy();
    }
  });
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

function fetchPage(link, level, animate = false) {
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
      el.style.opacity = 0;
      container.appendChild(el);
      stackPage(link, level);

      setTimeout(
        function (el, level) {
          el.dataset.level = level + 1;
          initializePage(el, level + 1);
          el.scrollIntoView();
          if (animate) {
            el.animate(
              [{ opacity: 0 }, { opacity: 1}],
              animationLength
            );
          }
          el.style.opacity = 1;
        }.bind(null, el, level),
        10
      );
    });
}

function createPreview(link, html) {
  let frame = document.createElement('iframe');
  frame.width = "400px";
  frame.height = "300px";
  frame.srcdoc = html;
  tippy(
    link, {
      allowHTML: true,
      content: frame.outerHTML,
      delay: 500,
      interactive: true,
      interactiveBorder: 10,
      inlinePositioning: false,
      maxWidth: "none",
      placement: "right",
      theme: "light",
      touch: ["hold", 500]
    }
  );
}

function initializePage(note, level) {
  targetBlank();

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
        createPreview(el, template.content.querySelector('.note').outerHTML);

        el.addEventListener('click', function (e) {
          if (!e.ctrlKey && !e.metaKey) {
            e.preventDefault();
            fetchPage(el.href, this.dataset.level, true);
          }
        });
      }
      updateLinkStatuses();
    }
  })
}

function targetBlank() {
  // remove subdomain of current site's url and setup regex
  var internal = location.host.replace("www.", "");
      internal = new RegExp(internal, "i");
      
  var a = document.getElementsByTagName('a'); // then, grab every link on the page
  for (var i = 0; i < a.length; i++) {
    var href = a[i].host; // set the host of each link
    if( !internal.test(href) ) { // make sure the href doesn't contain current site's host
      a[i].setAttribute('target', '_blank'); // if it doesn't, set attributes
    }
  }
};

window.addEventListener('popstate', function (_event) {
  // TODO: check state and pop pages if possible, rather than reloading.
  window.location = window.location; // this reloads the page.
});

window.onload = function() {
  initializePage(document.querySelector('.note'));

  let url = URI(window.location);
  if (url.hasQuery('stack')) {
    let stacks = url.query(true).stack;
    if (!Array.isArray(stacks)) {
      stacks = [stacks];
    }
    for (let i = 0; i < stacks.length; i++) {
      fetchPage(stacks[i], i + 1);
    }
  }
};
