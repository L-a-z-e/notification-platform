# DLQ 먼저 생성 (메인 큐가 참조)
resource "aws_sqs_queue" "dlq" {
  name                      = "${var.project_name}-events-dlq"
  message_retention_seconds = 345600 # 4일
}

# 메인 큐
resource "aws_sqs_queue" "events" {
  name                       = "${var.project_name}-events"
  visibility_timeout_seconds = 30
  message_retention_seconds  = 345600 # 4일

  redrive_policy = jsonencode({
    deadLetterTargetArn = aws_sqs_queue.dlq.arn
    maxReceiveCount     = 3
  })
}
