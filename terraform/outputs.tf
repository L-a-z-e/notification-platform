output "rds_endpoint" {
  description = "RDS 엔드포인트"
  value       = aws_db_instance.default.address
}

output "redis_endpoint" {
  description = "ElastiCache 엔드포인트"
  value       = aws_elasticache_cluster.default.cache_nodes[0].address
}

output "ecr_repository_url" {
  description = "ECR 저장소 URL"
  value       = aws_ecr_repository.default.repository_url
}

output "sqs_queue_url" {
  description = "SQS 큐 URL"
  value       = aws_sqs_queue.events.url
}

output "ecs_cluster_name" {
  description = "ECS 클러스터 이름"
  value       = aws_ecs_cluster.default.name
}
