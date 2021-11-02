# Ktor + Postgres + Nomad + Consul + Fabio

Esse projeto é um teste prático de microserviços usando o framework [Ktor](https://ktor.io), o banco de dados [Postgres](https://www.postgresql.org/), o orquestrador de conteiners [Nomad](https://www.nomadproject.io/), o *Service Mash* [Consul](https://www.consul.io/) e o *Load Balancer* para Consul [Fabio](https://fabiolb.net/).

> ATENÇÃO: Esse projeto não busca ser o padrão para a criação de microserviços usando as tecnologias listadas, é apenas um experimento meu enquanto buscava aprender mais sobre as tecnologias acima.

# Arquitetura

## Microserviços

São dois microserviços Ktor, sendo:

- `user-microservice`: Microserviço referente á criação, leitura (de todos e de um único registro), edição e remoção de usuários. Rota sendo `/user`;
- `post-microservice`: Microserviço referente á criação, leitura (de todos e de um único registro), edição e remoção de postagens. Rota sendo `/post`;

Cada um com quatro instâncias.

## Banco de dados

O banco de dados é um Postgres na sua última versão disponível no DockerHub. Com uma única instância.

## Fabio

Fabio é o *Load Balancer* da aplicação, que irá receber as requisições. Nesse caso, A rota do *Load Balancer* é 9999, e a rota da UI do Fabio é 9998.

# Executando

Primeiro, vá á pasta `nomad` e inicie o Consul, que será usado para fazer o *Networking* da aplicação:

```
sudo consul agent -dev
```

Após isso, inicie o Nomad, também na pasta `nomad`:

```
sudo nomad agent -dev-connect -config=nomad.hcl
```

Agora, inicie os *jobs*:

```
nomad job run postgres.nomad
nomad job run fabio.nomad
nomad job run user-microservice.nomad
nomad job run post-microservice.nomad
```
