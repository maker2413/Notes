#!/bin/bash

docker pull maker2413/orgroampublish:latest

docker run --rm -v $(pwd):/opt/OrgFiles maker2413/orgroampublish publish.el
