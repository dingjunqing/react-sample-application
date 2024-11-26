import dayjs from 'dayjs';

export interface IAIE {
  id?: string;
  name?: string;
  type?: string;
  description?: string | null;
  createdAt?: dayjs.Dayjs;
  createdBy?: string;
  icon?: string | null;
  version?: string | null;
  category?: string | null;
  rate?: number | null;
  aieMetadata?: string;
  userID?: string;
  isPublic?: boolean;
  organizationName?: string | null;
  tenantID?: string | null;
}

export const defaultValue: Readonly<IAIE> = {
  isPublic: false,
};
