# Etapa 1: Construção da aplicação
# Utiliza uma imagem do Maven para compilar a aplicação
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app

# Copia o arquivo pom.xml e baixa as dependências
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia o código fonte da aplicação e compila
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Execução da aplicação
# Utiliza uma imagem menor com o JDK apenas para rodar a aplicação
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copia o JAR da etapa de build para o diretório final
COPY --from=build /app/target/demo-0.0.1-SNAPSHOT.jar demo-app.jar

# Expõe a porta em que a aplicação Spring Boot será executada (geralmente 8080)
EXPOSE 8080

# Define o comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "demo-app.jar"]