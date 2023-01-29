ALTER TABLE IF EXISTS public.participants
    ADD COLUMN m_name character varying(120);

DROP VIEW public.participants_full CASCADE;

CREATE OR REPLACE VIEW public.participants_full
    AS
     SELECT participants.birth_date,
    participants.height,
    participants.weight,
    participants.s_name,
    participants.rank,
    participants.f_name,
    participants.m_name,
    participants.participant_id
   FROM participants;

ALTER TABLE public.participants_full
    OWNER TO postgres;