-- Add default config for existing tenants
UPDATE tenants 
SET config = jsonb_build_object(
    'timezone', 'UTC',
    'currency', 'USD',
    'emailNotifications', true,
    'smsNotifications', false,
    'maxUsers', 
        CASE subscription_tier
            WHEN 'FREE' THEN 3
            WHEN 'STARTER' THEN 10
            WHEN 'PROFESSIONAL' THEN 50
            WHEN 'ENTERPRISE' THEN 500
            ELSE 3
        END,
    'maxShipmentsPerMonth',
        CASE subscription_tier
            WHEN 'FREE' THEN 100
            WHEN 'STARTER' THEN 1000
            WHEN 'PROFESSIONAL' THEN 10000
            WHEN 'ENTERPRISE' THEN 100000
            ELSE 100
        END
)
WHERE config IS NULL;