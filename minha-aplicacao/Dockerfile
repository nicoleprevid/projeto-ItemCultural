# Utiliza uma imagem oficial do OpenJDK para Java 17
FROM openjdk:17-jdk-alpine

# Define o diretório de trabalho dentro do contêiner
WORKDIR /app

# Copia o arquivo .jar da aplicação para o contêiner
COPY target/minha-aplicacao-1.0-SNAPSHOT.jar /app/minha-aplicacao-1.0-SNAPSHOT.jar

# Define a porta que a aplicação usará
EXPOSE 8080

# Comando para executar o arquivo JAR
ENTRYPOINT ["java", "-jar", "/app/minha-aplicacao-1.0-SNAPSHOT.jar"]
