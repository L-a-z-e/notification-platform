resource "aws_ecr_repository" "default" {
  name                 = "${var.project_name}-platform"
  image_tag_mutability = "MUTABLE"
  force_delete         = true

  tags = {
    Name = "${var.project_name}-platform"
  }
}
