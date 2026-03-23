# RDS 서브넷 그룹
resource "aws_db_subnet_group" "default" {
  name       = "${var.project_name}-db-subnet"
  subnet_ids = slice(data.aws_subnets.default.ids, 0, 2)

  tags = {
    Name = "${var.project_name}-db-subnet"
  }
}

# RDS PostgreSQL
resource "aws_db_instance" "default" {
  identifier     = "${var.project_name}-db"
  engine         = "postgres"
  engine_version = "16"
  instance_class = "db.t4g.micro"

  allocated_storage = 20
  storage_type      = "gp2"
  storage_encrypted = false

  db_name  = var.db_name
  username = var.db_username
  password = var.db_password

  db_subnet_group_name   = aws_db_subnet_group.default.name
  vpc_security_group_ids = [aws_security_group.rds.id]
  publicly_accessible    = false
  multi_az               = false

  backup_retention_period = 0 # 백업 비활성화 (비용 절약)
  skip_final_snapshot     = true
  deletion_protection     = false

  tags = {
    Name = "${var.project_name}-db"
  }
}
