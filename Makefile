.PHONY: dev
build:
	@docker run \
		--rm \
		-v $(shell pwd):/opt/OrgFiles \
		maker2413/orgroampublish \
		publish.el

dev:
	@docker run \
		-p 8080:80 \
		-d \
		-v $(shell pwd)/Website:/usr/share/nginx/html:ro \
		--name notes-web \
		--rm \
		nginx

stop:
	@docker stop notes-web
