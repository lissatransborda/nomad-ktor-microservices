job "postgres" {
  datacenters = ["dc1"]

  group "db" {
    network {
      mode = "bridge"
    }

    volume "data" {
      type = "host"
      read_only = false
      source = "host"
    }

    service {
      name = "postgres"
      port = "5432"

      connect {
        sidecar_service {}
      }
    }

    task "postgres" {
      driver = "docker"
      volume_mount {
        volume = "data"
        destination = "/var/lib/postgres"
        read_only = false
      } 
      env {
        POSTGRES_DB = "db"
        POSTGRES_USER = "root"
        POSTGRES_PASSWORD = "root"
      }
      config {
        image = "postgres"
      }
      resources {
        cpu = 800
        memory = 1024
      }
    }
  }
}
