.PHONY: dev
dev:
	@docker run \
		--rm \
		-v $(shell pwd):/opt/OrgFiles \
		maker2413/orgroampublish \
		publish.el

webserver:
	@docker run \
		-p 8080:80 \
		-d \
		-v $(shell pwd)/Website:/usr/share/nginx/html:ro \
		--name notes-web \
		--rm \
		nginx

webserverstop:
	@docker stop notes-web
