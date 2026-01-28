CREATE TABLE IF NOT EXISTS tenants(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    subdomain VARCHAR(100) UNIQUE NOT NULL,
    subscription_tier VARCHAR(50) NOT NULL DEFAULT 'FREE',
    subscription_status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
    stripe_customer_id VARCHAR(100),
    max_users INTEGER DEFAULT 5,
    max_shipments_per_month INTEGER DEFAULT 1000,
    max_ai_queries_per_day INTEGER DEFAULT 10,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_tenants_subdomain ON tenants(subdomain);
CREATE INDEX idx_tenants_subscription_status ON tenants(subscription_status);