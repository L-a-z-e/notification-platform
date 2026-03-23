# ElastiCache 서브넷 그룹
resource "aws_elasticache_subnet_group" "default" {
  name       = "${var.project_name}-redis-subnet"
  subnet_ids = slice(data.aws_subnets.default.ids, 0, 2)
}

# ElastiCache Redis
resource "aws_elasticache_cluster" "default" {
  cluster_id           = "${var.project_name}-redis"
  engine               = "redis"
  node_type            = "cache.t3.micro"
  num_cache_nodes      = 1
  port                 = 6379
  subnet_group_name    = aws_elasticache_subnet_group.default.name
  security_group_ids   = [aws_security_group.redis.id]

  tags = {
    Name = "${var.project_name}-redis"
  }
}
