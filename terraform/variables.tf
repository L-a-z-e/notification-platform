variable "region" {
  description = "AWS 리전"
  type        = string
  default     = "ap-northeast-2"
}

variable "aws_profile" {
  description = "AWS CLI 프로필"
  type        = string
  default     = "notification-laze"
}

variable "project_name" {
  description = "프로젝트 이름 (리소스 네이밍에 사용)"
  type        = string
  default     = "notification"
}

variable "environment" {
  description = "환경 (dev, prod)"
  type        = string
  default     = "prod"
}

# RDS
variable "db_username" {
  description = "RDS 마스터 사용자명"
  type        = string
  sensitive   = true
}

variable "db_password" {
  description = "RDS 마스터 비밀번호"
  type        = string
  sensitive   = true
}

variable "db_name" {
  description = "초기 데이터베이스 이름"
  type        = string
  default     = "notification"
}

# ElastiCache
variable "redis_auth_token" {
  description = "Redis AUTH 토큰"
  type        = string
  sensitive   = true
  default     = ""
}

# ECS
variable "container_port" {
  description = "컨테이너 포트"
  type        = number
  default     = 8090
}

variable "my_ip" {
  description = "내 공인 IP (Security Group 인바운드 제한용, CIDR 형식)"
  type        = string
}
