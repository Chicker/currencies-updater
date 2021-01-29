CREATE TABLE IF NOT EXISTS exchange_rates (
  exchange_from  CHAR(3) NOT NULL,
  exchange_to    CHAR(3) NOT NULL,
  rate           NUMERIC NOT NULL,
  updated        TIMESTAMPTZ NOT NULL,
  PRIMARY KEY (exchange_from, exchange_to)
);
