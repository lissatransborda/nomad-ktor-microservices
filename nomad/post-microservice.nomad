job "post-microservice" {
  datacenters = ["dc1"]
  type = "service"

  group "post-microservice" {
    count = 4

    update {
      max_parallel = 2
      min_healthy_time = "30s"
      healthy_deadline = "5m"
    }

    network {
      mode = "bridge"
      port "http" {
        to = 4501
      }
    }

    service {
      name = "post-microservice"
      tags = ["urlprefix-/post"]
      port = "http"
      check {
        name     = "alive"
        type     = "http"
        path     = "/post"
        interval = "10s"
        timeout  = "2s"
      }
    }

    service{
      connect {
        sidecar_service {
          proxy {
            upstreams {
              destination_name = "postgres"
              local_bind_port  = 5432
            }
            config {
              protocol = "http"
            }
          }
        }
      }
    }

    restart {
      attempts = 2
      interval = "30m"
      delay = "15s"
      mode = "fail"
    }

    task "post-microservice" {
      env {
        DB_SERVICE_URL = "http://${NOMAD_UPSTREAM_ADDR_postgres}"
        PORT = 4501
      }

      driver = "docker"

      config {
        image = "edersonferreira/kotlinautas-artigo-nomad-post-microservice"
        ports = ["http"]
      }
    }
  }
}
