FROM nginx:stable-alpine

COPY ./conf.d/app.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx","-g","daemon off;"]