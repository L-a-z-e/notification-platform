# 앱 (ECS EC2) Security Group
resource "aws_security_group" "app" {
  name        = "${var.project_name}-app-sg"
  description = "ECS app instance"
  vpc_id      = data.aws_vpc.default.id

  # API 접근 (내 IP만)
  ingress {
    from_port   = var.container_port
    to_port     = var.container_port
    protocol    = "tcp"
    cidr_blocks = [var.my_ip]
    description = "App API from my IP"
  }

  # SSH (내 IP만)
  ingress {
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = [var.my_ip]
    description = "SSH from my IP"
  }

  # Outbound: HTTPS (AWS 서비스 — SQS, ECR, CloudWatch)
  egress {
    from_port   = 443
    to_port     = 443
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
    description = "HTTPS to AWS services"
  }

  # Outbound: PostgreSQL
  egress {
    from_port       = 5432
    to_port         = 5432
    protocol        = "tcp"
    security_groups = [aws_security_group.rds.id]
    description     = "PostgreSQL to RDS"
  }

  # Outbound: Redis
  egress {
    from_port       = 6379
    to_port         = 6379
    protocol        = "tcp"
    security_groups = [aws_security_group.redis.id]
    description     = "Redis to ElastiCache"
  }

  tags = {
    Name = "${var.project_name}-app-sg"
  }
}

# RDS Security Group
resource "aws_security_group" "rds" {
  name        = "${var.project_name}-rds-sg"
  description = "RDS PostgreSQL access"
  vpc_id      = data.aws_vpc.default.id

  tags = {
    Name = "${var.project_name}-rds-sg"
  }
}

resource "aws_security_group_rule" "rds_ingress" {
  type                     = "ingress"
  from_port                = 5432
  to_port                  = 5432
  protocol                 = "tcp"
  source_security_group_id = aws_security_group.app.id
  security_group_id        = aws_security_group.rds.id
  description              = "PostgreSQL from app"
}

# ElastiCache Security Group
resource "aws_security_group" "redis" {
  name        = "${var.project_name}-redis-sg"
  description = "ElastiCache Redis access"
  vpc_id      = data.aws_vpc.default.id

  tags = {
    Name = "${var.project_name}-redis-sg"
  }
}

resource "aws_security_group_rule" "redis_ingress" {
  type                     = "ingress"
  from_port                = 6379
  to_port                  = 6379
  protocol                 = "tcp"
  source_security_group_id = aws_security_group.app.id
  security_group_id        = aws_security_group.redis.id
  description              = "Redis from app"
}
