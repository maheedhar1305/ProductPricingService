FROM openjdk:8-jre
    
WORKDIR /app

COPY ./build/libs/ProductPricingService.jar .
COPY ./deployment/entrypoint.sh .

RUN ["chmod", "+x", "./entrypoint.sh"]

EXPOSE 8080

ENTRYPOINT ["./entrypoint.sh"]