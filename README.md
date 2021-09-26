[![AppVeyor build status](https://ci.appveyor.com/api/projects/status/kayjdh5qtgymhoxr/branch/master?svg=true)](https://ci.appveyor.com/project/Rdatatable/data-table)

# JavaTest
Teste de seleção para vaga de Java

### Requisitos cumpridos

- [x] @Repository
- [x] @Entity
- [x] @Controller
- [x] Documentação

### Ferramentas utilizadas

* [Lombok](https://projectlombok.org/)
* [Spring JPA](https://spring.io/projects/spring-data-jpa)
* [MySql](https://www.mysql.com/)
* [Swagger](https://swagger.io/docs/)

## Começando

### Utilizando docker

1. Caso tenha o docker instalado basta digitar o comando na raiz do projeto para criar um container mysql

    ```sh
    sudo docker-compose up
    ```
    
2. Para iniciar a aplicação rode o comando no terminal

    ```sh
    mvn spring-boot:run
    ```

### Instalação padrão

1. Caso já o mysql rodando no computador, basta apenas mudar as credenciais no arquivo application.yml

   ```yml
   user: root
   password: root
   ```
   
2.  E para iniciar rode o comando

    ```sh
    mvn spring-boot:run
    ```
  
### Acessando a aplicação

Acesse a url ```localhost:8080/sigabem/api/v1/frete-calculator```


