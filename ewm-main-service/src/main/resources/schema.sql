DROP TABLE IF EXISTS users CASCADE;
CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name  VARCHAR(512) NOT NULL,
    email VARCHAR(512) NOT NULL,
    CONSTRAINT UQ_USER_NAME UNIQUE (name),
    CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

DROP TABLE IF EXISTS categories CASCADE;
CREATE TABLE IF NOT EXISTS categories
(
    id   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    CONSTRAINT UQ_CATEGORY_NAME UNIQUE (name)
);

DROP TABLE IF EXISTS locations CASCADE;
CREATE TABLE IF NOT EXISTS locations
(
    id  BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    lat FLOAT,
    lon FLOAT
);

DROP TABLE IF EXISTS events CASCADE;
CREATE TABLE IF NOT EXISTS events
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    annotation         VARCHAR(2000)               NOT NULL,
    category_id        BIGINT                      NOT NULL,
    confirmed_requests BIGINT,
    description        VARCHAR(7000)         NOT NULL,
    event_date         TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    location_id        BIGINT                      NOT NULL,
    paid               BOOLEAN                     NOT NULL,
    participant_limit  INT                         NOT NULL,
    request_moderation BOOLEAN                     NOT NULL,
    title              VARCHAR(120)                NOT NULL,
    initiator          BIGINT                      NOT NULL,
    created_on         TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
    published_on       TIMESTAMP WITHOUT TIME ZONE default NULL,
    state              VARCHAR(120)                  NOT NULL,
    views              BIGINT                      NOT NULL,
    CONSTRAINT fk_events_to_categories FOREIGN KEY (category_id) REFERENCES categories (id) ON DELETE CASCADE,
    CONSTRAINT fk_events_to_locations FOREIGN KEY (location_id) REFERENCES locations (id) ON DELETE CASCADE,
    CONSTRAINT fk_events_to_users FOREIGN KEY (initiator) REFERENCES users (id) ON DELETE CASCADE
);

DROP TABLE IF EXISTS requests CASCADE;
CREATE TABLE IF NOT EXISTS requests
(
    id           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    event_id     BIGINT NOT NULL,
    requester_id BIGINT NOT NULL,
    created      TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
    status       VARCHAR(9),
    CONSTRAINT fk_requests_to_events FOREIGN KEY (event_id) REFERENCES events (id) ON DELETE CASCADE,
    CONSTRAINT fk_requests_to_users FOREIGN KEY (requester_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT UQ_REQUESTER_ID_BY_EVENT_ID UNIQUE (requester_id, event_id)
);

DROP TABLE IF EXISTS compilations CASCADE;
CREATE TABLE IF NOT EXISTS compilations
(
    id     BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title  VARCHAR(2048) NOT NULL,
    pinned BOOLEAN     NOT NULL,
    CONSTRAINT UQ_COMPILATION_TITLE UNIQUE (title)
);

DROP TABLE IF EXISTS compilations_events CASCADE;
CREATE TABLE IF NOT EXISTS compilations_events
(
    compilation_id BIGINT NOT NULL,
    event_id       BIGINT NOT NULL,
    CONSTRAINT fk_compilations_events_to_compilations
    FOREIGN KEY (compilation_id) REFERENCES compilations (id) ON DELETE CASCADE,
    CONSTRAINT fk_compilations_events_to_events
    FOREIGN KEY (event_id) REFERENCES events (id) ON DELETE CASCADE,
    PRIMARY KEY (compilation_id, event_id)
);