(package-initialize)

(require 'use-package)

(use-package htmlize
  :ensure t)

(use-package org-roam
  :ensure t
  :init
  ;; Disable v2 warning message
  (setq org-roam-v2-ack t)
  :custom
  ;; Roam Notes directory
  (org-roam-directory "/opt/OrgFiles")
  :config
  (org-roam-setup))

(require 'ox-html)
(require 'ox-publish)

;; Don't create backup files (those ending with ~) during the publish process.
(setq make-backup-files nil)

;; Configure Packages
(setq-default indent-tabs-mode nil)
(setq tab-width 2)
(setq c-basic-offset 2)
(setq sh-basic-offset 2)

(add-hook 'python-mode-hook
          (lambda()
            (setq tab-width 2)
            (setq python-indent-offset 2)))

;; Set Defaults
(setq base-dir "/opt/OrgFiles")
(setq images-dir (concat base-dir "/Images"))
(setq notes-dir (concat base-dir "/Content"))
(setq output-dir "/opt/OrgFiles/WebSite")
(setq images-output-dir (concat output-dir "/images"))

(setq maker/header "
<link rel='stylesheet' type='text/css' href='/css/style.css'/>
<link rel='stylesheet' type='text/css' href='https://unpkg.com/tippy.js@6/themes/light.css'/>
<script src='https://unpkg.com/@popperjs/core@2'></script>
<script src='https://unpkg.com/tippy.js@6'></script>
<script src='/js/script.js'></script>
<script src='/js/URI.js'></script>
")

(setq org-id-extra-files (org-roam-list-files))

;; Define the publishing project
(setq org-publish-project-alist
      `(("org-notes"
         :base-directory ,notes-dir
         :base-extension "org"
         :html-doctype "html5"
         :html-head-extra ,maker/header
         :html-head-include-default-style nil
         :html-head-include-scripts nil
         :html-validation-link nil
         :publishing-directory ,output-dir
         :publishing-function org-html-publish-to-html
         :section-numbers nil
         :recursive t
         :with-author nil)
        ("css-js"
         :base-directory ,base-dir
         :base-extension "css\\|js"
         :exclude "WebSite/.*"
         :publishing-directory ,output-dir
         :publishing-function org-publish-attachment
         :recursive t)
        ("images"
         :base-directory ,images-dir
         :base-extension "png"
         :publishing-directory ,images-output-dir
         :publishing-function org-publish-attachment
         :recursive t)
        ("website"
         :components ("org-notes" "css-js" "images"))))

;; Overwrite default HTML output template
;;
;; I would ideally like to switch to using ox-slimhtml,
;; but for the moment I don't see the ox-slimhtml package
;; available without storing the elisp script in this repo
(eval-after-load "ox-html"
  '(defun org-html-template (contents info)
     (concat (org-html-doctype info)
             "\n<html lan=\"en\">\n"
             "<head>\n"
             (org-html--build-meta-info info)
             (org-html--build-head info)
             (org-html--build-mathjax-config info)
             "<link rel='shortcut icon' type='image/png' href='./images/brain.png'/>\n"
             "</head>\n"
             "<body>\n"
             "<div class='notes-container'>\n"
             "<div class='notes'>\n"
             "<div class='note'>\n"
             ;; Document contents.
             (let ((div (assq 'content (plist-get info :html-divs))))
               (format "<%s class=\"%s\">\n" (nth 1 div) (nth 2 div)))
             ;; Document title.
             (when (plist-get info :with-title)
               (let ((title (and (plist-get info :with-title)
                                 (plist-get info :title)))
                     (subtitle (plist-get info :subtitle))
                     (html5-fancy (org-html--html5-fancy-p info)))
                 (when title
                   (format
                    (if html5-fancy
                        "<header>\n<h1 class=\"title\">%s</h1>\n%s</header>"
                      "<h1 class=\"title\">%s%s</h1>\n")
                    (org-export-data title info)
                    (if subtitle
                        (format
                         (if html5-fancy
                             "<p class=\"subtitle\">%s</p>\n"
                           (concat "\n" (org-html-close-tag "br" nil info) "\n"
                                   "<span class=\"subtitle\">%s</span>\n"))
                         (org-export-data subtitle info))
                      "")))))
             contents
             (format "</%s>\n" (nth 1 (assq 'content (plist-get info :html-divs))))
             "</div>\n"
             "</div>\n"
             "</div>\n"
             ;; Postamble.
             (org-html--build-pre/postamble 'postamble info)
             "</body>\n"
             "</html>\n"
             )))

;; Generate the site output
(org-publish-all t)
