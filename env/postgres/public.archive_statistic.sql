create table public.archive_statistic
(
    id                  serial primary key,
    ip_address          varchar not null,
    archive_usage_count integer not null,
    usage_date          date    not null
);

comment on table public.archive_statistic is 'Archive service usage statistic';

comment on column public.archive_statistic.ip_address is 'User IP address';
comment on column public.archive_statistic.archive_usage_count is 'Count for archive service usage';
comment on column public.archive_statistic.usage_date is 'Date of archive service usage';