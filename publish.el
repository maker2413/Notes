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
            (setq py-indent-offset 2)))

;; Set Defaults
(setq base-dir "/opt/OrgFiles")
(setq output-dir "/opt/OrgFiles/WebSite")

(setq maker/header "
<link rel='stylesheet' type='text/css' href='/css/style.css'/>")

(setq org-id-extra-files (org-roam-list-files))

;; Define the publishing project
(setq org-publish-project-alist
      `(("org-files"
         :base-directory ,base-dir
         :base-extension "org"
         :exclude ".*[Tt]emplates/.*\\|.*[Pp]rojects/.*\\|.*[Dd]ailies/.*\\|WebSite/.*"
         :html-doctype "html5"
         :html-head-extra ,maker/header
         :html-head-include-default-style nil
         :html-validation-link nil
         :publishing-directory ,output-dir
         :publishing-function org-html-publish-to-html
         :section-numbers nil
         :recursive t
         :with-author nil)
        ("css"
         :base-directory ,base-dir
         :base-extension "css"
         :exclude "WebSite/.*"
         :publishing-directory ,output-dir
         :publishing-function org-publish-attachment
         :recursive t)
        ("website"
         :components ("org-files" "css"))))

;; Overwrite default HTML output template
;; (eval-after-load "ox-html"
;;   '(defun org-html-template (contents info)
;;      (concat (org-html-doctype info)
;;              "<html lan=\"en\">
;;                 <head>"
;;              (org-html--build-meta-info info)
;;              (org-html--build-head info)
;;              (org-html--build-mathjax-config info)
;;              "</head>
;;               <body>"
;;              "<div class='note-container'>"
;;              )))

;; Generate the site output
(org-publish-all t)
