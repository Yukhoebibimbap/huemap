FROM node:14-alpine as builder

WORKDIR /app

ENV VUE_APP_URL https://huemap.shop

COPY package.json ./

RUN npm install

COPY ./ ./

RUN npm run build

FROM nginx:stable-alpine as production

COPY ./nginx/nginx.conf /etc/nginx/conf.d/default.conf

COPY --from=builder /app/dist /usr/share/nginx/html

CMD ["nginx","-g","daemon off;"]
