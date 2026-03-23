# 기본 VPC 사용 (새로 만들지 않음)
data "aws_vpc" "default" {
  default = true
}

# 기본 VPC의 퍼블릭 서브넷 조회
data "aws_subnets" "default" {
  filter {
    name   = "vpc-id"
    values = [data.aws_vpc.default.id]
  }

  filter {
    name   = "default-for-az"
    values = ["true"]
  }
}

# AZ 목록
data "aws_availability_zones" "available" {
  state = "available"
}
